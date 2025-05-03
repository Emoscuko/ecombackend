package com.example.ecommerce.repositories;

import java.util.Optional;

import com.example.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // for authentication lookup

}
