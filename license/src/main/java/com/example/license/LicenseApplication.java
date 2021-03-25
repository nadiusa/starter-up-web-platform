package com.example.license;

import com.example.license.entities.Role;
import com.example.license.repos.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LicenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicenseApplication.class, args);
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {

		return args -> {

			Role adminRole = roleRepository.findByRole("ADMIN");
			if (adminRole == null) {
				Role newAdminRole = new Role();
				newAdminRole.setRole("ADMIN");
				roleRepository.save(newAdminRole);
			}

//			Role userRole = roleRepository.findByRole("USER");
//			if (userRole == null) {
//				Role newUserRole = new Role();
//				newUserRole.setRole("USER");
//				roleRepository.save(newUserRole);
//			}
//
//			Role investorRole = roleRepository.findByRole("INVESTOR");
//			if (investorRole == null) {
//				Role newInvestorRole = new Role();
//				newInvestorRole.setRole("INVESTOR");
//				roleRepository.save(newInvestorRole);
//			}
//
//			Role entrepreneurRole = roleRepository.findByRole("ENTREPRENEUR");
//			if (entrepreneurRole == null) {
//				Role newEntrepreneurRole = new Role();
//				newEntrepreneurRole.setRole("ENTREPRENEUR");
//				roleRepository.save(newEntrepreneurRole);
//			}
		};

	}

}
