package org.example.backend.controller;

import org.example.backend.entity.User;
import org.example.backend.entity.UserDTO;
import org.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class StudentController {

    private final AuthenticationManager authenticationManager;
    private UserService userService;

    public StudentController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    //test authorization
    @GetMapping("/hi")
    public String sayHello() {
        return "Hi";
    }

    @GetMapping("/all")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/all")
    public User createUser (@RequestBody UserDTO userDTO) {

        return userService.saveUser(userDTO);

    }

    @PostMapping("/login")
    public String loginUser (@RequestBody UserDTO userDTO) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(), userDTO.getPassword()
                ));

        if (authentication.isAuthenticated()) {
            return "Token will be returning in Avengers Doomsday";
        } else {
            return "Login failed";
        }

    }


}
