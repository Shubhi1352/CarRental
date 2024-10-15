package com.shubhi.dto;

import java.util.List;

import lombok.Data;

@Data
public class CarDtoListDto {
	private List<CarDto> carDtoList;

	public List<CarDto> getCarDtoList() {
		return carDtoList;
	}

	public void setCarDtoList(List<CarDto> carDtoList) {
		this.carDtoList = carDtoList;
	}
	
}
