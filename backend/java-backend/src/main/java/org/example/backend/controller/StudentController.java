package org.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class StudentController {

    //test authorization
    @GetMapping("/hi")
    public ResponseEntity<?> sayHello(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

}
