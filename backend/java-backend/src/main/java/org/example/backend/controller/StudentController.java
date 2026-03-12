package org.example.backend.controller;

import org.example.backend.entity.User;
import org.example.backend.entity.UserDTO;
import org.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class StudentController {

    private UserService userService;

    public StudentController(UserService userService) {
        this.userService = userService;
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


}
