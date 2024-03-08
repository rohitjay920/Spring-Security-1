package com.project.filter;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.service.UserService;
import com.project.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
			
			//if the jwt token does not exist then then the jwtfilter will not be checked further and the request will be passed in to next filter 
			//i.e, UsernamePasswordAuthenticationFilter
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		log.debug("JWT-{}", jwt.toString());
		userEmail = jwtUtils.extractUserName(jwt);
		System.err.println("The user email is: "+userEmail);
		if (!(StringUtils.isEmpty(userEmail)) && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			System.err.println("Hello");
			
			UserDetails details = userService.userDetailsService().loadUserByUsername(userEmail);
			
			System.err.println(jwtUtils.isTokenValid(jwt, details));
			
			if (jwtUtils.isTokenValid(jwt, details)) {
				log.debug("User-{}", details);
				//creating a empty security context
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details, null,
						details.getAuthorities());
				//setting details is IP address , session ID by creating the object of WebAuthenticationDetailsSource
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				//setting UsernamePasswordAuthenticationToken to SecurityContext
				context.setAuthentication(authToken);
				//setting security context containing the user details, password, granted authorities and IP address etc to SecurityOntextHolder
				SecurityContextHolder.setContext(context);
			}
			else {
				System.err.println("Token is invalid");
			}

		}
		//forwarding the request to the next filter
		filterChain.doFilter(request, response);
	}
}
