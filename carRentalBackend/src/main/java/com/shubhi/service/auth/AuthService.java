package com.shubhi.service.auth;

import com.shubhi.dto.SignupRequest;
import com.shubhi.dto.UserDto;

public interface AuthService {
	boolean hasCustomerWithEmail(String email);
	
	UserDto createCustomer(SignupRequest signupRequest);
}
