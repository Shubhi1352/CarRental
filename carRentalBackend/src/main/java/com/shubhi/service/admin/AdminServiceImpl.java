package com.shubhi.service.admin;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.shubhi.dto.BookACarDto;
import com.shubhi.dto.CarDto;
import com.shubhi.dto.CarDtoListDto;
import com.shubhi.dto.SearchCarDto;
import com.shubhi.entity.BookACar;
import com.shubhi.entity.Car;
import com.shubhi.enums.BookCarStatus;
import com.shubhi.repository.BookACarRepository;
import com.shubhi.repository.CarRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private BookACarRepository bookACarRepository;

	@Override
	public boolean postCar(CarDto carDto) throws IOException {
		try {
			Car car=new Car();
			car.setName(carDto.getName());
			car.setBrand(carDto.getBrand());
			car.setColor(carDto.getColor());
			car.setDescription(carDto.getDescription());
			car.setImage(carDto.getImage().getBytes());
			car.setPrice(carDto.getPrice());
			car.setTransmission(carDto.getTransmission());
			car.setType(carDto.getType());
			car.setYear(carDto.getYear());
			System.out.println(car);
			carRepository.save(car);
			return true;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}

	@Override
	public List<CarDto> getAllCars() {
		return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
	}

	@Override
	public void deleteCar(Long id) {
		carRepository.deleteById(id);
	}

	@Override
	public CarDto getCarById(Long id) {
		Optional<Car> optionalCar=carRepository.findById(id);
		if(optionalCar.isPresent()) {
			return optionalCar.get().getCarDto();
		}
		return null;
	}

	@Override
	public boolean updateCar(Long carId, CarDto carDto) throws IOException {
		Optional<Car> optionalCar=carRepository.findById(carId);
		if(optionalCar.isPresent()) {
			Car existingCar=optionalCar.get();
			if(carDto.getImage()!=null) {
				existingCar.setImage(carDto.getImage().getBytes());
			}
			existingCar.setPrice(carDto.getPrice());
			existingCar.setYear(carDto.getYear());
			existingCar.setType(carDto.getType());
			existingCar.setDescription(carDto.getDescription());
			existingCar.setTransmission(carDto.getTransmission());
			existingCar.setColor(carDto.getColor());
			existingCar.setName(carDto.getName());
			existingCar.setBrand(carDto.getBrand());
			carRepository.save(existingCar);
			return true;
		}
		else return false;
	}

	@Override
	public List<BookACarDto> getBookings() {
		return bookACarRepository.findAll().stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
	}

	@Override
	public boolean changeBookingStatus(long bookingId, String status) {
		Optional<BookACar> optionalBookACar=bookACarRepository.findById(bookingId);
		if(optionalBookACar.isPresent()) {
			BookACar existingBookACar=optionalBookACar.get();
			existingBookACar.setBookCarStatus(status.equals("Approve")?BookCarStatus.APPROVED:BookCarStatus.REJECTED);
			bookACarRepository.save(existingBookACar);
			return true;
		}
		return false;
	}

	@Override
	public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
		System.out.println(searchCarDto.getBrand()+" "+searchCarDto.getColor()+" "+searchCarDto.getType()+" "+searchCarDto.getTransmission());
	    Car car = new Car();
	    if (searchCarDto.getBrand() != null) car.setBrand(searchCarDto.getBrand());    
	    if (searchCarDto.getColor() != null) car.setColor(searchCarDto.getColor());
	    if (searchCarDto.getType() != null) car.setType(searchCarDto.getType());
	    if (searchCarDto.getTransmission() != null) car.setTransmission(searchCarDto.getTransmission());

	    ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
	        .withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
	        .withMatcher("type", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
	        .withMatcher("color", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
	        .withMatcher("transmission", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

	    Example<Car> carExample = Example.of(car, exampleMatcher);
	    List<Car> carList = carRepository.findAll(carExample);
	    CarDtoListDto carDtoListDto = new CarDtoListDto(); 
	    carDtoListDto.setCarDtoList(
	        carList.stream().map(Car::getCarDto).collect(Collectors.toList())
	    ); 
	    return carDtoListDto;
	}

	
}
