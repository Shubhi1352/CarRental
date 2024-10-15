package com.shubhi.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.*;
import jakarta.servlet.http.*;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter{
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain)throws IOException, ServletException{
		HttpServletResponse response=(HttpServletResponse) res;
		HttpServletRequest request=(HttpServletRequest) req;
		Map<String, String> map=new HashMap<>();
		String originHeader=request.getHeader("origin");
		response.setHeader("Access-Control-Allow-Origin", originHeader);
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "7200");
		response.setHeader("Access-Control-Allow-Headers", "*");
		
		if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		}else {
			chain.doFilter(req, res);
		}
	}
	
	
	@Override
	public void init(FilterConfig filterConfig) {
		
	}
	
	@Override
	public void destroy() {
		
	}
}
