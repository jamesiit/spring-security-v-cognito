package org.example.backend.controller;

import org.example.backend.entity.User;
import org.example.backend.entity.UserDTO;
import org.example.backend.service.JWTService;
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
    private JWTService jwtService;

    public StudentController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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

        //creating the temporary token
        //AuthenticationManager will now take the username and password and give it to the DaoProvider for verification
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(), userDTO.getPassword()
                ));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userDTO.getUsername());
        } else {
            return "Login failed";
        }

    }


}
