package test;

import Entity.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repo.CartRepository;

/*public class CargoTest {

	CartRepository cartRepository;

	@BeforeEach
	void before() {
        cartRepository = new CartRepository();
	}

	@Test
	void insertTest() {
        CartItem cartItem = CartItem.builder().
		List<CargoDto> result = null;
		try {
			result = cargoRepo.selectAllCargo();
			assertEquals(result.size(), 5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/*	@Test
	void selectAllCargoCount() {
		List<CargoDto> result = null;
		try {
			result = cargoRepo.selectAllCargoCount();
			assertEquals(result.size(), 2);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}*/