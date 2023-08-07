package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dto.CargoDto;
import repo.CargoRepository;


public class CargoTest {
	
	CargoRepository cargoRepo;
	
	@BeforeEach
	void before() {
		cargoRepo = new CargoRepository();
	}
	
	@Test
	void selectAllCargoTest() {
		List<CargoDto> result = null;
		try {
			result = cargoRepo.selectAllCargo();
			assertEquals(result.size(), 5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	void selectAllCargoCount() {
		List<CargoDto> result = null;
		try {
			result = cargoRepo.selectAllCargoCount();
			assertEquals(result.size(), 2);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
