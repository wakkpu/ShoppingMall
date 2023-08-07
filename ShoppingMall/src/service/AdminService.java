package service;

import java.util.List;

import Entity.Cargo;
import Entity.OrderSet;
import dto.CargoDto;
import repo.AdminOrderRepository;
import repo.CargoRepository;
import repo.StatusRepository;

public class AdminService {

	AdminOrderRepository adminOrderRepository;
	StatusRepository statusRepository;
	CargoRepository cargoRepository;
	
	public AdminService() {
		adminOrderRepository = new AdminOrderRepository();
		statusRepository = new StatusRepository();
		cargoRepository = new CargoRepository();
	}
	
	public List<OrderSet> getAllOrders() throws Exception {
		return adminOrderRepository.selectAll();
	}
	
	public List<OrderSet> getOrderSetsByStatusId(Long statusId) throws Exception {
		return adminOrderRepository.selectOrderSetsByStatusId(statusId);
	}
	
	// TODO need to be transaction
	public void registerCargo(CargoDto cargoDto) {
		try {
			// cargoDto로부터 item_name, item_price 조회
			// (category_id, item_name, item_price) -> item 생성
			
			// item_name을 통해 item_id 조회
			Long itemId = 0L;
			//Long itemId =  itemRepo.getItemIdFromItemName(CargoDto.getItemName());
			
			// category_name을 통해 category_id 조회
			Long statusId = statusRepository.selectStatusIdByName(cargoDto.getStatusName());
			
			// cargo 생성
			for(int i=0; i<cargoDto.getCargoCount(); i++) {
				cargoRepository.insert(
							Cargo.builder()
							.itemId(itemId)
							.statusId(statusId)
							.build()
						);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
