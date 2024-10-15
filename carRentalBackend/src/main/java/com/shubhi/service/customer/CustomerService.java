package com.shubhi.service.customer;

import java.util.List;

import com.shubhi.dto.BookACarDto;
import com.shubhi.dto.CarDto;
import com.shubhi.dto.CarDtoListDto;
import com.shubhi.dto.SearchCarDto;

import io.jsonwebtoken.io.IOException;

public interface CustomerService {
	List<CarDto> getAllCars();
	boolean bookACar(BookACarDto bookACarDto) throws IOException;
	CarDto getCarById(long carId);
	List<BookACarDto> getBookingsByUserId(long userId);
	CarDtoListDto searchCar(SearchCarDto searchCarDto);

}
