package com.example.ecommerce;

import com.example.ecommerce.enums.Role;
import com.example.ecommerce.models.*;
import com.example.ecommerce.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner {
	@Autowired private UserRepository userRepo;
	@Autowired private CategoryRepository categoryRepo;
	@Autowired private ProductRepository productRepo;
	@Autowired private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Create default admin if not exists
		if(userRepo.findByEmail("admin@shop.com").isEmpty()) {
			User admin = new User();
			admin.setName("Administrator");
			admin.setEmail("admin@shop.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole(Role.ADMIN);
			admin.setEnabled(true);
			userRepo.save(admin);
		}
		// Create sample categories and products
		if(categoryRepo.count() == 0) {
			Category cat1 = new Category(null, "Electronics", "Electronic gadgets",null);
			Category cat2 = new Category(null, "Books", "Books of all genres",null);
			categoryRepo.saveAll(List.of(cat1, cat2));
			// Sample products


		}
	}
}
