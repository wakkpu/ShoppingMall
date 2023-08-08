package service;

import java.util.List;

import dto.CargoDto;
import repo.CargoRepository;
import repo.StatusRepository;

public class CargoService {
	
	CargoRepository cargoRepository;
	
	StatusRepository statusRepository;
	
	public CargoService() {
		cargoRepository = new CargoRepository();
		statusRepository = new StatusRepository();
	}
	
	public List<CargoDto> selectAllCargo() throws Exception {
		return cargoRepository.selectAllCargo();
	}
	
	public List<CargoDto> selectAllCargoCount() throws Exception {
		return cargoRepository.selectAllCargoCount();
	}

}
