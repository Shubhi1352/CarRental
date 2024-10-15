package com.shubhi.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shubhi.dto.SignupRequest;
import com.shubhi.dto.UserDto;
import com.shubhi.entity.User;
import com.shubhi.enums.UserRole;
import com.shubhi.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImple implements AuthService {
	@Autowired
	private UserRepository userRepository;
	
	
	@PostConstruct
	public void createAdminAccount() {
		User adminAccount=userRepository.findByUserRole(UserRole.ADMIN);
		if(adminAccount==null) {
			User newAdminAccount=new User();
			newAdminAccount.setName("Admin");
			newAdminAccount.setEmail("admin@gmail.com");
			newAdminAccount.setPassword(new BCryptPasswordEncoder().encode("Admin"));
			newAdminAccount.setUserRole(UserRole.ADMIN);
			userRepository.save(newAdminAccount);
			System.out.println("Admin Account Created Successfully!");
		}
	}

	@Override
	public UserDto createCustomer(SignupRequest signupRequest) {
		User user=new User();
		user.setName(signupRequest.getName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
		user.setUserRole(UserRole.CUSTOMER);
		User createduser=userRepository.save(user);
		UserDto userDto=new UserDto();
		userDto.setId(createduser.getId());
		return userDto;
	}
	
	public boolean hasCustomerWithEmail(String email) {
		if(userRepository.findByEmail(email)!=null) return true;
		return false;
	}
}
