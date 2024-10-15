package com.shubhi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubhi.entity.User;
import com.shubhi.enums.UserRole;


@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	public User findByEmail(String email);

	Optional<User> findFirstByEmail(String username);

	public User findByUserRole(UserRole userRole);
}
