package org.example.backend.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
public class OtpGeneratorService {

    // 
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateOTP() {

        return secureRandom.ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());

    }


}
