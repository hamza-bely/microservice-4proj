package com.traficandme.user_service_traficandme;

import com.traficandme.user_service_traficandme.user.dto.UserRequest;
import com.traficandme.user_service_traficandme.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceTraficandmeApplication {

	@Bean
	public CommandLineRunner createDefaultUsers(UserService userService) {
		return args -> {
			if (userService.getOneUserByEmail("admin@traficandme.com") == null) {
				userService.createUser(new UserRequest(null,"Admin", "User", "admin@traficandme.com", "AdminPass123!", "ADMIN","ACTIVE"));
				System.out.println("Admin added.");
			}

			if (userService.getOneUserByEmail("hamza.bely@traficandme.com") == null) {
				userService.createUser(new UserRequest(null,"Hamza", "Bely", "hamza.bely@traficandme.com", "UserPass123!", "USER","ACTIVE"));
				System.out.println("User added.");
			}
		};
	}


	public static void main(String[] args) {
		SpringApplication.run(UserServiceTraficandmeApplication.class, args);
	}

}
