package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.LoginDto;
import com.restaurent.RMS.dtos.response.AuthenticationResponseDto;
import com.restaurent.RMS.entities.RestaurantPrivilege;
import com.restaurent.RMS.entities.Token;
import com.restaurent.RMS.entities.User;
import com.restaurent.RMS.exceptionHandlers.AccessRevokedException;
import com.restaurent.RMS.exceptionHandlers.BusinessRuleViolationException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.AuthenticationMapper;
import com.restaurent.RMS.repositories.RestaurantPrivilegeRepository;
import com.restaurent.RMS.repositories.TokenRepository;
import com.restaurent.RMS.repositories.UserRepository;
import com.restaurent.RMS.utils.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationMapper authenticationMapper;
    private final RestaurantPrivilegeRepository restaurantPrivilegeRepository;

    // DO NOT make final; Spring will inject the property value
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private final String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!()_?<>]).{8,}$";

    @Transactional
    public AuthenticationResponseDto login(LoginDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email " + request.getEmail()));

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String jwtToken = jwtService.generateToken(user.getEmail());

        Token token = new Token();
        token.setToken(jwtToken);
        token.setType("Bearer");
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusSeconds(jwtExpiration / 1000));
        token.setRevoked(false);
        tokenRepository.save(token);

        AuthenticationResponseDto authenticationResponseDto = authenticationMapper.toAuthenticationResponse(user);
        authenticationResponseDto.setAccessToken(jwtToken);
        authenticationResponseDto.setExpiresIn(jwtExpiration / 1000);
        authenticationResponseDto.setUserId(user.getId());

        return authenticationResponseDto;
    }

    public void updatePassword(String email, String newPassword, String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessRuleViolationException(ValidationMessages.PASSWORD_MISMATCH);
        }

        if (!newPassword.matches(regex)) {
            throw new BusinessRuleViolationException(ValidationMessages.PASSWORD_COMPLEXITY);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email"));

        String oldPassword = user.getPassword();

        if (bCryptPasswordEncoder.matches(newPassword, oldPassword)) {
            throw new IllegalArgumentException(ValidationMessages.NEW_PASSWORD_SAME_AS_OLD);
        }

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
