package com.project.dto;

import com.project.enums.Role;

import lombok.Data;

@Data
public class LoginDto {
	Role role; 
	String jwtToken;
}
