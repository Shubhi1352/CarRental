package com.shubhi.service.auth.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
	UserDetailsService userDetailsService();
}
