package org.example.backend.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
public class OtpGeneratorService {

    // initializes once on startup
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateOTP() {

        return secureRandom.ints(6, 0, 10) // generate a stream of 6 integers between 0 and 10
                .mapToObj(String::valueOf) // takes each int and converts it into a String
                .collect(Collectors.joining()); // takes all Strings and converts it into one String

    }


}
