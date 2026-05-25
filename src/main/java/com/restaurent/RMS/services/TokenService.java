package com.restaurent.RMS.services;

import com.restaurent.RMS.entities.Token;
import com.restaurent.RMS.entities.User;
import com.restaurent.RMS.exceptionHandlers.*;
import com.restaurent.RMS.repositories.TokenRepository;
import com.restaurent.RMS.utils.ValidationMessages;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDateTime;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;


    public Token createTokenForUser(User user, int otp, int expiryMinutes) {
        Token token = new Token();
        token.setToken(String.valueOf(otp));
        token.setType("OTP");
        token.setUser(user);
        token.setRevoked(false);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(expiryMinutes));
        return tokenRepository.save(token);
    }


    public Token getOtpForUser(User user) {
        Token token = tokenRepository.findTopByUserIdAndTypeOrderByCreateAtDesc(user.getId(), "OTP");

        if (token == null) {
            throw new ResourceNotFoundException("No OTP found this user");
        }
        return token;
    }

    public void revokeOtp(User user) {
        Token token = tokenRepository
                .findTopByUserIdAndTypeOrderByCreateAtDesc(user.getId(), "OTP");

        if (token != null) {
            token.setRevoked(true);
            tokenRepository.save(token);
        }
    }

    public void revokeToken(String tokenValue) {

        Token token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new TokenInvalidException(ValidationMessages.TOKEN_INVALID_));

        if (token.getRevoked()) {
            throw new TokenRevokedException(ValidationMessages.TOKEN_REVOKED);
        }

        token.setRevoked(true);
        tokenRepository.save(token);
    }


    public void validateotp(User user,int enteredotp){
        Token token=getOtpForUser(user);
        if(token.getRevoked()){
            throw new AlreadyExistException("This OTP has already been used.");
        }
        if(token.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new UnauthorizedException("OTP has expired.");
        }

        if(!token.getToken().equals(String.valueOf(enteredotp))){
            throw new IllegalArgumentException("OTP is incorrect.");
        }
    }
}
