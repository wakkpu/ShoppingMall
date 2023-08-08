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
import java.util.logging.Logger;

import Entity.Cargo;
import Entity.Item;
import Entity.OrderDetail;
import Entity.OrderSet;
import dto.CancelInput;
import dto.CargoDto;
import dto.ItemQuntityDto;
import dto.OrderInputDTO;
import resources.ConnectionPool;

public class OrderRepositoty {

	ConnectionPool connectionPool;
	Logger logger = Logger.getLogger("Cargo Repository");

	OrderSetRepository orderSetDao;
	OrderDetailRepository orderDeatailDao;
	CargoRepository cargoRepository;
	ItemRepository itemRepository;
	StatusRepository statusRepository;

	public OrderRepositoty() {
		try {
			connectionPool = ConnectionPool.create();
			orderSetDao = new OrderSetRepository();
			orderDeatailDao = new OrderDetailRepository();
			cargoRepository = new CargoRepository();
			itemRepository = new ItemRepository();
			statusRepository = new StatusRepository();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int order(Long consumerId, List<OrderInputDTO> orderInputList, String orderAddress) throws Exception {

		// db
		int result = 0;
		Connection con = connectionPool.getConnection();
		con.setAutoCommit(false);
		PreparedStatement pstmt = null;

		try {
			// 1. insert order set
			Random random = new Random();
			String orderCode = "PRO" + Integer.toString(random.nextInt(10000));
			LocalDateTime now = LocalDateTime.now();
			OrderSet orderSet = OrderSet.builder().consumerId(consumerId).orderCode(orderCode).orderTime(now)
					.orderAddress(orderAddress).build();

			long orderSetId = orderSetDao.insertSetOrder(con, orderSet);

			// 2. chago item select
			for (OrderInputDTO orderInput : orderInputList) {

				long itemId = orderInput.getItemId();

				// 상품 정보 들고오기
				Item item = itemRepository.selectItem(itemId).get();
				Long statusId = statusRepository.selectStatusIdByName("입고"); 

				// 재고 확인
				List<CargoDto> cargoList = cargoRepository.selectCargoId(con, itemId, orderInput.getItemQuantity());

				int itemInvenQuantity = cargoList.size();

				if (itemInvenQuantity < orderInput.getItemQuantity()) {
					// 실패
					String itemName = itemRepository.selectItem(orderInput.getItemId()).get().getItemName();
					orderInput.getItemId(); // 실패한 아이템
					throw new Exception(itemName + "재고가 부족합니다.");
				}

				List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();

				for (CargoDto cargo : cargoList) {

					// 주문 수량 만큼 데이터 상태 업데이트
					int resultUpdateStatus = cargoRepository.updateState(con, cargo.getCargoId()); // 창고 수량 업데이트

					orderDetailList.add(OrderDetail.builder().orderSetId(orderSetId).cargoId(cargo.getCargoId())
							.statusId(statusId).buyPrice(item.getItemPrice()).build());
				}

			}
			con.commit();

		} catch (Exception e) {
			con.rollback();

			throw new Exception("주문 실패 : " + e.getMessage());

		} finally {
			CRUDRepository.closePreparedStatement(pstmt);
			connectionPool.releaseConnection(con);
		}

		return result;
	}

	public List<OrderSet> selectOrderListByConsumerId(Long consumerId) throws Exception {

		Connection conn = connectionPool.getConnection();

		PreparedStatement pstmt = null;
		List<OrderSet> orderList = new ArrayList<OrderSet>();
		ResultSet rset = null;

		try {

			pstmt = conn.prepareStatement("select * from order_set");

			rset = pstmt.executeQuery();

			while (rset.next()) {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
				LocalDateTime dateTime = LocalDateTime.parse(rset.getString(4), formatter);

				orderList.add(
						new OrderSet(rset.getLong(1), rset.getLong(2), rset.getString(3), dateTime, rset.getString(5)));
			}

		} catch (Exception e) {
			throw new Exception("전체 조회 에러 ");
		} finally {
			CRUDRepository.closeResultSet(rset);
			CRUDRepository.closePreparedStatement(pstmt);
			connectionPool.releaseConnection(conn);
		}

		return orderList;
	}

	public List<ItemQuntityDto> selectDetial(Long orderSetId) throws Exception {

		Connection conn = connectionPool.getConnection();
		PreparedStatement pstmt = null;
		List<ItemQuntityDto> item = new ArrayList<ItemQuntityDto>();
		ResultSet rset = null;

		try {

			pstmt = conn.prepareStatement("SELECT car.item_id, COUNT(car.item_id), MAX(det.buy_price) as item_quantity "
					+ "FROM cargo car "
					+ "JOIN  order_detail det "
					+ "ON car.cargo_id = det.cargo_id "
					+ "WHERE det.order_set_id = ? "
					+ "GROUP BY car.itemId");
			pstmt.setLong(1, orderSetId);

			rset = pstmt.executeQuery();

			while (rset.next()) {

				item.add(new ItemQuntityDto(rset.getLong(1), rset.getLong(2), rset.getLong(3)));
			}

		} catch (Exception e) {
			throw new Exception("상세 조회 에러 ");
		} finally {
			CRUDRepository.closeResultSet(rset);
			CRUDRepository.closePreparedStatement(pstmt);
			CRUDRepository.closePreparedStatement(pstmt);
			connectionPool.releaseConnection(conn);
		}

		return item;
	}

	public int deleteOrder(List<CancelInput> cancelInputList) throws Exception {

		int result = 0;
		Connection con = connectionPool.getConnection();
		PreparedStatement pstmtSelect = null;

		PreparedStatement pstmtUpdate = null;
		ResultSet rsetSelect = null;
		ResultSet rsetUpdate = null;
		con.setAutoCommit(false);

		try {
			pstmtUpdate = con.prepareStatement("UPDATE order_detail ord SET ord.status_id=? WHERE ord.cargo_id=?");

			for (CancelInput cancelInput : cancelInputList) {
				long itemId = cancelInput.getItemId();
				
				Long statusBefId = statusRepository.selectStatusIdByName("결제 완료");

				pstmtSelect = con.prepareStatement("select os.cargo_id " + "from order_set os "
						+ "join order_detail od on os.order_set_id = od.order_set_id "
						+ "join cargo c on od.cargo_id = c.cargo_id " + "join item i on i.item_id = c.item_id "
						+ "where i.item_id = ? and i.status_id = ?" + "limit ?");
				pstmtSelect.setLong(1, itemId);
				pstmtSelect.setLong(2, statusBefId);
				pstmtSelect.setLong(3, cancelInput.getModifyQuantity());

				rsetSelect = pstmtSelect.executeQuery();
				Long statusAftId = statusRepository.selectStatusIdByName("입고");
				
				// 주문 상세 update
				while (rsetSelect.next()) {

					pstmtUpdate.setLong(1, statusAftId);
					pstmtUpdate.setLong(2, rsetSelect.getLong(1));
					rsetUpdate = pstmtUpdate.executeQuery();

				}

				con.commit();

			}
		} catch (Exception e) {
			con.rollback();

			throw new Exception("주문 취소 에러 ");
		} finally {
			CRUDRepository.closeResultSet(rsetSelect);
			CRUDRepository.closeResultSet(rsetUpdate);
			CRUDRepository.closePreparedStatement(pstmtSelect);
			CRUDRepository.closePreparedStatement(pstmtUpdate);

			connectionPool.releaseConnection(con);
		}

		return result;
	}

}
