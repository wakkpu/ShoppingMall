package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import Entity.OrderSet;
import dto.CargoDto;
import service.AdminService;
import service.CargoService;

public class AdminMain {

	static AdminService adminService = new AdminService();
	static CargoService cargoService = new CargoService();
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException {
		
		
		
		
	}
	
	// 입고
	public static void registerCargo() throws IOException {
		System.out.println("상품 목록에 추가할 상품의 이름을 입력하세요");
		String itemName = br.readLine();
		
		System.out.println("상품 목록에 추가할 상품의 이름을 입력하세요");
		Long itemPrice = Long.parseLong(br.readLine());
		
		System.out.println("상품의 입고 수량을 입력하세요");
		Long cargoCount = Long.parseLong(br.readLine());
		
		System.out.println("상품 목록에 추가할 상품의 분류 코드를 입력하세요");
		Long categoryId = Long.parseLong(br.readLine());
		
		adminService.registerCargo(
				CargoDto.builder()
				.itemName(itemName)
				.itemPrice(itemPrice)
				.cargoCount(cargoCount)
				.categoryId(categoryId)
				.build()
		);
	}
	
	// 모든 재고 조회
	public static void getAllCargoInfo() throws Exception {
		List<CargoDto> list = cargoService.selectAllCargo();
		
		System.out.println("재고 번호\t상품명\t재고 상태");
		for(CargoDto cargo: list) {
			System.out.println(cargo.getCargoId()+"\t"+cargo.getItemName()+"\t"+cargo.getStatusName());
		}
	}
	
	// (상품명, 재고량) 목록 조회
	public static void getAllCargoCounts() throws Exception {
		List<CargoDto> list = cargoService.selectAllCargoCount();
		
		System.out.println("상품명\t재고량");
		for(CargoDto cargo: list) {
			System.out.println(cargo.getItemName()+"\t"+cargo.getCargoCount());
		}
	}
	
	// 전체 주문 조회
	public static void getAllOrders() throws Exception {
		List<OrderSet> allOrders = adminService.getAllOrders();
		
		for(OrderSet order: allOrders) {
			System.out.println(order.toString());
		}
	}
	
	// 상태별 주문 조회
	public static void getOrdersByStatusId() throws Exception {
		System.out.println("어떤 상태의 재고를 확인하시겠습니까?");
		Long statusId = Long.parseLong(br.readLine());
		
		List<OrderSet> orders = adminService.getOrderSetsByStatusId(statusId);
		
		for(OrderSet order: orders) {
			System.out.println(order.toString());
		}
	}
	
	
}
