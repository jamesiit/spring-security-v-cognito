package org.example.backend.service;

import org.example.backend.entity.VerificationToken;
import org.example.backend.repo.VerificationTokenRepo;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private VerificationTokenRepo tokenRepo;

    public TokenService(VerificationTokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    public VerificationToken verifyToken(String otpString) {
        return tokenRepo.findByOtpString(otpString);
    }
}
