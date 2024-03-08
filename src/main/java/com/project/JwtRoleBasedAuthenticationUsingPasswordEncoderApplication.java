package com.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.entity.User;
import com.project.enums.Role;
import com.project.repository.UserRepository;

@SpringBootApplication
public class JwtRoleBasedAuthenticationUsingPasswordEncoderApplication implements CommandLineRunner{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(JwtRoleBasedAuthenticationUsingPasswordEncoderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(userRepository.count()==0) {
			User user = new User();
			user.setEmail("admin123@gmail.com");
			user.setPassword(passwordEncoder.encode("admin123"));
			user.setRole(Role.ADMIN);
			user.setUsername("Rohit");
			
			userRepository.save(user);
		}
		
	}
}
