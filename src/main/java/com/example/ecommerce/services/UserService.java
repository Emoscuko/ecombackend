package com.example.ecommerce.services;

import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired private UserRepository userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User updateUser(Long id, User updatedData) {
        User user = getUserById(id);
        // update allowed fields
        user.setName(updatedData.getName());
        // do not update email or password here (could have separate methods for that)
        return userRepo.save(user);
    }
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User setUserEnabled(Long id, boolean enabled) {
        User user = userRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id)
                );
        user.setEnabled(enabled);  // field defined here :contentReference[oaicite:0]{index=0}&#8203;:contentReference[oaicite:1]{index=1}
        return userRepo.save(user);
    }

}
