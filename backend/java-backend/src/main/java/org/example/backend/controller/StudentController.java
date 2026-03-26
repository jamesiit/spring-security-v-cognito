package org.example.backend.controller;

import org.example.backend.entity.User;
import org.example.backend.entity.UserDTO;
import org.example.backend.entity.VerificationToken;
import org.example.backend.repo.VerificationTokenRepo;
import org.example.backend.service.JWTService;
import org.example.backend.service.MailService;
import org.example.backend.service.OtpGeneratorService;
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
    private VerificationTokenRepo verificationTokenRepo;
    private OtpGeneratorService otpGenerator;
    private MailService mailService;

    public StudentController(AuthenticationManager authenticationManager, UserService userService, JWTService jwtService, VerificationTokenRepo verificationTokenRepo, OtpGeneratorService otpGenerator, MailService mailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.verificationTokenRepo = verificationTokenRepo;
        this.otpGenerator = otpGenerator;
        this.mailService = mailService;
    }

    //test authorization and email
    @GetMapping("/hi")
    public String sayHello() {
        mailService.sendSimpleMessage();
        return "Hi";
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

        verificationTokenRepo.save(verificationToken);

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


}
