package com.shubhi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubhi.entity.Car;


@Repository
public interface CarRepository extends JpaRepository<Car,Long>{

}
