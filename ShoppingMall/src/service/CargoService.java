package service;

import repo.CargoRepository;
import repo.StatusRepository;

public class CargoService {
	
	CargoRepository cargoRepository;
	
	StatusRepository statusRepository;
	
	public CargoService() {
		cargoRepository = new CargoRepository();
		statusRepository = new StatusRepository();
	}

}
