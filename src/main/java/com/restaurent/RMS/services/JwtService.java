package com.restaurent.RMS.services;

import com.restaurent.RMS.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Autowired
    private JwtUtil jwtUtil;
    public String generateToken(String email){
        UserDetails userDetails = User.withUsername(email)
                .password("")
                .authorities("USER")
                .build();
        return jwtUtil.generateToken(userDetails);
    }
    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
    }

    public Boolean validateToken(String token, String username) {
        UserDetails userDetails = User.withUsername(username)
                .password("")
                .authorities("USER")
                .build();

        return jwtUtil.validateToken(token, userDetails);
    }
}
