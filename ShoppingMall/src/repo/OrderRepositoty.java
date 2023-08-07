package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Entity.Cargo;
import Entity.Consumer;
import Entity.Item;
import Entity.OrderDetail;
import Entity.OrderSet;
import dto.CancelInput;
import dto.OrderInputDTO;
import util.ConnectionPool;

public class OrderRepositoty {

	ConnectionPool cp;

	OrderSetRepository orderSetDao;
	OrderDetailRepository orderDeatailDao;
	CargoRepository cargoRepository;
	ItemRepository itemRepository;

	public OrderRepositoty() {
		try {
			cp = ConnectionPool.create();
			orderSetDao = new OrderSetRepository();
			orderDeatailDao = new OrderDetailRepository();
			cargoRepository = new CargoRepository();
			itemRepository = new ItemRepository();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 바로 주문
	 */
	public int order(Long consumerId, List<OrderInputDTO> orderInputList, String orderAddress) throws Exception {

		// db
		int result = 0;
		Connection con = cp.getConnection();
		con.setAutoCommit(false);
		PreparedStatement pstmt = null;

		try {
			// 1. insert order set
			Random random = new Random();
			String num = Integer.toString(random.nextInt(10000));
			String orderCode = "PRO" + num;
			LocalDateTime now = LocalDateTime.now();
			OrderSet orderSet = OrderSet.builder().consumerId(consumerId).orderCode(orderCode)
					.orderTime(now).orderAddress(orderAddress).build();

			long orderSetId = orderSetDao.insertSetOrder(con, orderSet);

			// 2. chago item select
			for (OrderInputDTO orderInput : orderInputList) {

				long itemId = orderInput.getItemId();

				// 상품 정보 들고오기
				Item item = itemRepository.selectItem(itemId).get();
				Long statusId = 6L; // 주문 상태에 대한 상태 정보 들고오기 :: 변경 해야 함.

				// 재고 확인
				List<Cargo> cargoList = cargoRepository.selectCargo(con, itemId, orderInput.getItemQuantity()); 

				int itemInvenQuantity = cargoList.size();

				if (itemInvenQuantity < orderInput.getItemQuantity()) {
					// 실패
					String itemName = itemRepository.selectItem(orderInput.getItemId()).get().getItemName();
							orderInput.getItemId(); // 실패한 아이템  
					throw new Exception(itemName + "재고가 부족합니다.");
				}

				List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();

				for (Cargo cargo : cargoList) {

					// 주문 수량 만큼 데이터 상태 업데이트
					int resultUpdateStatus = cargoRepository.updateState(con, cargo.getCargoId()); // 창고 수량 업데이트

					orderDetailList.add(OrderDetail.builder().orderSetId(orderSetId).cargoId(cargo.getCargoId())
							.statusId(statusId).buyPrice(item.getItemPrice()).build());
				}

			}

		} catch (Exception e) {
			con.rollback();
			
			throw new Exception("주문 실패 : " + e.getMessage());

		} finally {
			CRUDRepository.closePstmt(pstmt);
			cp.releaseConnection(con);
		}

		return result;
	}

	public List<OrderSet> selectOrderListByConsumerId(Long consumerId) throws Exception {

		Connection con = cp.getConnection();
		con.setAutoCommit(false);

		PreparedStatement pstmt = null;
		List<OrderSet> orderList = new ArrayList<OrderSet>();
		ResultSet rset = null;

		try {

			pstmt = con.prepareStatement("select * from order_set");

			rset = pstmt.executeQuery();

			while (rset.next()) {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
				LocalDateTime dateTime = LocalDateTime.parse(rset.getString(3), formatter);

				orderList.add(
						new OrderSet(rset.getLong(1), rset.getLong(2), rset.getString(3), dateTime, rset.getString(5)));
			}

		} catch (Exception e) {
			con.rollback();

			throw new Exception("전체 조회 에러 ");
		} finally {
			CRUDRepository.closeRset(rset);
			CRUDRepository.closePstmt(pstmt);
			cp.releaseConnection(con);
		}

		return orderList;
	}

	public List<OrderDetail> selectDetial(Long orderSetId) throws Exception {

		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		List<OrderDetail> orderDetial = new ArrayList<OrderDetail>();
		ResultSet rset = null;
		con.setAutoCommit(false);

		try {

			pstmt = con.prepareStatement("select * from order_set ord where ord.order_set_id = ?");

			rset = pstmt.executeQuery();

			while (rset.next()) {

				orderDetial.add(new OrderDetail(rset.getLong(1), rset.getLong(2), rset.getLong(3), rset.getLong(4),
						rset.getLong(5)));
			}

		} catch (Exception e) {
			con.rollback();

			throw new Exception("상세 조회 에러 ");
		} finally {
			CRUDRepository.closeRset(rset);
			CRUDRepository.closePstmt(pstmt);
			cp.releaseConnection(con);
		}

		return orderDetial;
	}

	public int deleteOrder(List<CancelInput> cancelInputList) throws Exception {

		int result = 0;
		int n = 0;
		Connection con = cp.getConnection();
		PreparedStatement pstmtSelect = null;

		PreparedStatement pstmtUpdate = null;
		ResultSet rsetSelect = null;
		ResultSet rsetUpdate = null;
		con.setAutoCommit(false);

		try {
			pstmtUpdate = con.prepareStatement("UPDATE order_detail ord SET ord.status_id=? WHERE ord.cargo_id=?");

			for (CancelInput cancelInput : cancelInputList) {
				long itemId = cancelInput.getItemId();

				pstmtSelect = con.prepareStatement("select os.cargo_id " + "from order_set os "
						+ "join order_detail od on os.order_set_id = od.order_set_id "
						+ "join cargo c on od.cargo_id = c.cargo_id " + "join item i on i.item_id = c.item_id "
						+ "where i.item_id = ? and i.status_id = ?" + "limit ?");
				pstmtSelect.setLong(1, itemId);
				pstmtSelect.setLong(2, itemId);

				rsetSelect = pstmtSelect.executeQuery();

				// 주문 상세 update
				while (n < cancelInput.getModifyQuantity() && rsetSelect.next()) {

					pstmtUpdate.setLong(1, 4L);
					pstmtUpdate.setLong(2, rsetSelect.getLong(1));
					rsetUpdate = pstmtUpdate.executeQuery();
					n++;

				}

				con.commit();

			}
		} catch (Exception e) {
			con.rollback();

			throw new Exception("삭제 에러 ");
		} finally {
			CRUDRepository.closeRset(rsetSelect);
			CRUDRepository.closeRset(rsetUpdate);
			CRUDRepository.closePstmt(pstmtSelect);
			CRUDRepository.closePstmt(pstmtUpdate);

			cp.releaseConnection(con);
		}

		return result;
	}

}
