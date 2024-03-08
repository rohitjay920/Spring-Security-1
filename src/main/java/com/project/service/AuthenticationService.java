package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.dto.LoginDto;
import com.project.dto.SignInDto;
import com.project.dto.SignUpDto;
import com.project.entity.User;
import com.project.enums.Role;
import com.project.repository.UserRepository;
import com.project.utils.JwtUtils;

@Service
public class AuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtils jwt;
	
	public String signUp(SignUpDto request) {
		User user = new User();
		user.setEmail(request.getEmail());
		System.err.println("hello");
		System.err.println(request.getEmail());
		System.err.println(request.getPassword());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);
		user.setUsername(request.getUsername());
		
		userRepository.save(user);
		return "User saved successfully";
		
	}
	
	public LoginDto signIn(SignInDto request) {
		UserDetails userDetails = userService.userDetailsService().loadUserByUsername(request.getUsername());
		String token = jwt.generateToken(userDetails);
		LoginDto login = new LoginDto();
		login.setRole(((User)userDetails).getRole());
		login.setJwtToken(token);
		return login;
	}
}
