package com.project.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.LoginDto;
import com.project.dto.SignInDto;
import com.project.dto.SignUpDto;
import com.project.entity.User;
import com.project.service.AuthenticationService;
import com.project.service.UserService;

@RestController
public class UserController {
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/save")
	public ResponseEntity<String> signUp(@RequestBody SignUpDto request){
		String message = authenticationService.signUp(request);
		return new ResponseEntity<String>(message,HttpStatus.CREATED);
	}
	
	
	@PostMapping("/signin")
	public ResponseEntity<LoginDto> login(@RequestBody SignInDto request){
		LoginDto login = authenticationService.signIn(request);
		return new ResponseEntity<LoginDto>(login,HttpStatus.OK); 
	}
	
	
	@GetMapping("/getAll")
	public ResponseEntity<List<User>> getAll(){
		List<User> list = userService.getAll();
		return new ResponseEntity<List<User>>(list,HttpStatus.OK);
 	}
}
