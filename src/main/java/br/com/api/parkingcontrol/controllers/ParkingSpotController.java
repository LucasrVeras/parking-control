package br.com.api.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.api.parkingcontrol.dtos.ParkingSpotDto;
import br.com.api.parkingcontrol.models.ParkingSpotModel;
import br.com.api.parkingcontrol.services.ParkingSpotService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {
	
	final ParkingSpotService parkingSpotService;
	
	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid
			ParkingSpotDto parkingSpotDto){
		
		 if(parkingSpotService
				 .existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())){
	            return ResponseEntity
	            		.status(HttpStatus.CONFLICT)
	            		.body("Conflict: License Plate Car is already in use!");
	        }
	        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())){
	            return ResponseEntity
	            		.status(HttpStatus.CONFLICT)
	            		.body("Conflict: Parking Spot is already in use!");
	        }
	        if(parkingSpotService
	        		.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
	            return ResponseEntity
	            		.status(HttpStatus.CONFLICT)
	            		.body("Conflict: Parking Spot already registered for this apartment/block!");
	        }
		
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(parkingSpotService.save(parkingSpotModel));
	}
	
	//Busca paginada
	@GetMapping
	public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(
			@PageableDefault(page = 0, 
			size = 10, 
			sort = "id", 
			direction = Sort.Direction.ASC) Pageable pageable){
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(parkingSpotService
				.findAll(pageable));
	}
	
}
