package org.example.backend.service;

import org.example.backend.entity.User;
import org.example.backend.entity.UserDTO;
import org.example.backend.repo.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepo userRepo;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        System.out.println(user.getPassword());
        return userRepo.save(user);
    }

    public User checkUserByUsername(String otpUsername) {
        return userRepo.findByUsername(otpUsername);
    }


    public User saveUpdatedUser(User checkUser) {
        return userRepo.save(checkUser);
    }
}
