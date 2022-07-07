package br.com.api.parkingcontrol.services;

import org.springframework.stereotype.Service;

import br.com.api.parkingcontrol.models.ParkingSpotModel;
import br.com.api.parkingcontrol.repositories.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {
	
	final ParkingSpotRepository parkingSpotRepository;

	public Object save(ParkingSpotModel parkingSpotModel) {
		
		return null;
	}
}
