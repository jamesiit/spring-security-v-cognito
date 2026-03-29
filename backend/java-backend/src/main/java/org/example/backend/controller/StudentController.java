package org.example.backend.controller;

import org.example.backend.entity.OtpDTO;
import org.example.backend.entity.User;
import org.example.backend.entity.UserDTO;
import org.example.backend.entity.VerificationToken;
import org.example.backend.service.*;
import org.springframework.http.HttpStatus;
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
    private OtpGeneratorService otpGenerator;
    private MailService mailService;
    private TokenService tokenService;

    public StudentController(AuthenticationManager authenticationManager, UserService userService, JWTService jwtService, OtpGeneratorService otpGenerator, MailService mailService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.otpGenerator = otpGenerator;
        this.mailService = mailService;
        this.tokenService = tokenService;
    }

    //test authorization and email
    @GetMapping("/hi")
    public ResponseEntity<String> sayHello() {
        mailService.sendSimpleMessage();
        return new ResponseEntity<>("Hi", HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public User createUser (@RequestBody UserDTO userDTO) {

        // password hashing logic
        User savedUser = userService.saveUser(userDTO);

        // generate OTP
        String newCode = otpGenerator.generateOTP();

        // initialize entity
        VerificationToken verificationToken = new VerificationToken(newCode, savedUser);

        // save token
        tokenService.saveToken(verificationToken);

        return savedUser;

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

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody OtpDTO otpDTO) {

        // check if user exists first
        User checkUser = userService.checkUserByUsername(otpDTO.getOtpUsername());

        if (checkUser == null) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.OK);
        }

        //check if token is valid
        VerificationToken token = tokenService.verifyToken(otpDTO.getOtpString());

        if (token == null) {
            return new ResponseEntity<>("Invalid OTP!", HttpStatus.BAD_REQUEST);
        }

        // check if token belongs to user
        if (checkUser.getId() != token.getUser().getId()) {
            return new ResponseEntity<>("OTP not valid for user", HttpStatus.BAD_REQUEST);
        }

        // check if token is expired
        if (token.isExpired()) {
            return new ResponseEntity<>("time has expired! try again", HttpStatus.BAD_REQUEST);
        }

        checkUser.setEnabled(true);

        // save updated user to database
        User updatedUser = userService.saveUpdatedUser(checkUser);

        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);

    }


}
