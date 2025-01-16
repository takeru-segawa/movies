package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(User user) {
        Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
        Optional<User> userOptionalEmail = userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent() || userOptionalEmail.isPresent()) {
            throw new RuntimeException("User exist");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }


}
