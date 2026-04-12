package org.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class StudentController {

    //test authorization
    @GetMapping("/hi")
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity<>("Hi", HttpStatus.OK);
    }

}
