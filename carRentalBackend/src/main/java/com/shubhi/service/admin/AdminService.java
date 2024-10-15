package com.shubhi.service.admin;

import java.io.IOException;
import java.util.List;

import com.shubhi.dto.BookACarDto;
import com.shubhi.dto.CarDto;
import com.shubhi.dto.CarDtoListDto;
import com.shubhi.dto.SearchCarDto;

public interface AdminService {
	boolean postCar(CarDto carDto) throws IOException;
	List<CarDto> getAllCars();
	void deleteCar(Long id);
	CarDto getCarById(Long id);
	boolean updateCar(Long carId,CarDto carDto) throws IOException;
	List<BookACarDto> getBookings();
	boolean changeBookingStatus(long bookingId,String status);
	CarDtoListDto searchCar(SearchCarDto searchCarDto);
}
