package com.shubhi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.shubhi.dto.AuthenticationRequest;
import com.shubhi.dto.AuthenticationResponse;
import com.shubhi.dto.SignupRequest;
import com.shubhi.dto.UserDto;
import com.shubhi.entity.User;
import com.shubhi.repository.UserRepository;
import com.shubhi.service.auth.AuthService;
import com.shubhi.service.auth.jwt.UserService;
import com.shubhi.utils.JWTUtil;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupCustomer(@RequestBody SignupRequest signupRequest){
		if(authService.hasCustomerWithEmail(signupRequest.getEmail())) return new ResponseEntity<>("This Email Already exists :(",HttpStatus.BAD_REQUEST);
		
		UserDto createdCustomerDto=authService.createCustomer(signupRequest);
		if(createdCustomerDto==null) return new ResponseEntity<>("Customer not created, Come Back Again :(",HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(createdCustomerDto,HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)throws BadCredentialsException, DisabledException, UsernameNotFoundException{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
		}catch(BadCredentialsException e){
			throw new BadCredentialsException("Incorrect Username or password :( ");
		}
		final UserDetails userDetails=userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
		Optional<User> optionalUser=userRepository.findFirstByEmail(userDetails.getUsername());
		final String jwt=jwtUtil.generateToken(userDetails);
		AuthenticationResponse authenticationResponse=new AuthenticationResponse();
		if(optionalUser.isPresent()) {
			authenticationResponse.setJwt(jwt);
			authenticationResponse.setUserId(optionalUser.get().getId());
			authenticationResponse.setUserRole(optionalUser.get().getUserRole());
		}
		return authenticationResponse;
	}

}
