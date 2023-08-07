package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import Entity.Status;
import repo.StatusRepository;

public class StatusTest {

	StatusRepository statusRepo = new StatusRepository();
	
	@Test
	public void getStatusByName() {
		try {
			Long status = statusRepo.selectStatusIdByName("ют╟М");
			assertEquals(1, status);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	void selectAllStatus() {
		List<Status> result = new ArrayList<>();
		
		try {
			result = statusRepo.selectAll();
			assertEquals(result.size(), 1);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getAllStatus() {
		try {
			List<Status> result = statusRepo.selectAll();
			assertEquals(1, result.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
