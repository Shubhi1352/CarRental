package com.shubhi.service.customer;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
import com.shubhi.entity.User;
import com.shubhi.enums.BookCarStatus;
import com.shubhi.repository.BookACarRepository;
import com.shubhi.repository.CarRepository;
import com.shubhi.repository.UserRepository;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookACarRepository bookACarRepository;

	@Override
	public List<CarDto> getAllCars() {
		
		return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
	}

	@Override
	public boolean bookACar(BookACarDto bookACarDto) throws IOException {
		Optional<Car> optionalCar=carRepository.findById(bookACarDto.getCarId());
		Optional<User> optionalUser=userRepository.findById(bookACarDto.getUserId());
		try {
		if(optionalCar.isPresent() && optionalUser.isPresent()) {
			BookACar bookACar=new BookACar();
			bookACar.setFromDate(bookACarDto.getFromDate());
			bookACar.setToDate(bookACarDto.getToDate());
			bookACar.setUser(optionalUser.get());
			bookACar.setCar(optionalCar.get());
			bookACar.setBookCarStatus(BookCarStatus.PENDING);
			long diffInMilliSeconds=bookACar.getToDate().getTime()-bookACar.getFromDate().getTime();
			long days=(diffInMilliSeconds)/(1000*60*60*24);
			System.out.println(days);
			bookACar.setDays(days);
			bookACar.setPrice(optionalCar.get().getPrice()*days);
			bookACarRepository.save(bookACar);
			return true;
		}}catch(Exception e) {
			System.out.println(e.getMessage());	
		}
		return false;
	}

	@Override
	public CarDto getCarById(long carId) {
		Optional<Car> optionalCar=carRepository.findById(carId);
		if(optionalCar.isPresent()) return optionalCar.get().getCarDto();
		return null;
	}

	@Override
	public List<BookACarDto> getBookingsByUserId(long userId) {
		return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
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
