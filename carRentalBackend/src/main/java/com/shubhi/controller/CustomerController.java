package com.shubhi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubhi.dto.BookACarDto;
import com.shubhi.dto.CarDto;
import com.shubhi.dto.SearchCarDto;
import com.shubhi.service.customer.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/cars")
	public ResponseEntity<List<CarDto>> getAllCars(){
		List<CarDto> carDtoList=customerService.getAllCars();
		return ResponseEntity.ok(carDtoList);
	}
	
	@PostMapping("/car/book")
	public ResponseEntity<Void> bookACar(@RequestBody BookACarDto bookACarDto){
		System.out.println(bookACarDto.getFromDate());
		System.out.println(bookACarDto.getToDate());
		boolean success=customerService.bookACar(bookACarDto);
		if(success) return ResponseEntity.status(HttpStatus.CREATED).build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@GetMapping("/car/{carId}")
	public ResponseEntity<CarDto> getCarById(@PathVariable long carId){
		CarDto carDto=customerService.getCarById(carId);
		if(carDto!=null) return ResponseEntity.ok(carDto);
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/car/bookings/{userId}")
	public ResponseEntity<List<BookACarDto>> getBookingsByUserId(@PathVariable long userId){
		return ResponseEntity.ok(customerService.getBookingsByUserId(userId));
	}
	
	@PostMapping("/car/search")
	public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto){
		return ResponseEntity.ok(customerService.searchCar(searchCarDto));
	}

}
