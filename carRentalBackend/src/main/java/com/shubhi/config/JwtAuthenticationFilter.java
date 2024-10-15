package com.shubhi.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shubhi.service.auth.jwt.UserService;
import com.shubhi.utils.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
	throws ServletException, IOException, java.io.IOException{
		final String authHeader=request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		if(StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader,"Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
		jwt=authHeader.substring(7);
		userEmail=jwtUtil.extractUserName(jwt);
		}catch(ExpiredJwtException e) {
			 // Handle token expiration 
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.getWriter().write("JWT Token has expired.");
		    return;
		}catch (JwtException e) {
		    // Handle other JWT-related exceptions
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.getWriter().write("Invalid JWT Token.");
		    return;
		}
		if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails=userService.userDetailsService().loadUserByUsername(userEmail);
			if(jwtUtil.isTokenValid(jwt,userDetails)) {
				SecurityContext context=SecurityContextHolder.createEmptyContext();	
				UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
						userDetails,null,userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				context.setAuthentication(authToken);
				SecurityContextHolder.setContext(context);
			}
		}
		filterChain.doFilter(request, response);
	}
}
