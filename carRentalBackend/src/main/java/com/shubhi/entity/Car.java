package com.shubhi.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.shubhi.dto.CarDto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="cars")
public class Car {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String brand;
	private String color;
	private String name;
	private String type;
	private String transmission;
	private String description;
	private Long price;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
	private Date year;
	@Column(columnDefinition="longblob")
	private byte[] image;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTransmission() {
		return transmission;
	}
	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Date getYear() {
		return year;
	}
	public void setYear(Date year) {
		this.year = year;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public CarDto getCarDto() {
		CarDto carDto=new CarDto();
		carDto.setId(id);
		carDto.setName(name);
		carDto.setBrand(brand);
		carDto.setColor(color);
		carDto.setPrice(price);
		carDto.setDescription(description);
		carDto.setType(type);
		carDto.setTransmission(transmission);
		carDto.setYear(year);
		carDto.setReturnedImage(image);
		return carDto;
	}
}
