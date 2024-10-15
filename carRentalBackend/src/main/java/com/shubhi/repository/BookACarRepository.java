package com.shubhi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubhi.dto.BookACarDto;
import com.shubhi.entity.BookACar;

@Repository
public interface BookACarRepository extends JpaRepository<BookACar,Long> {

	List<BookACar> findAllByUserId(long userId);

}
