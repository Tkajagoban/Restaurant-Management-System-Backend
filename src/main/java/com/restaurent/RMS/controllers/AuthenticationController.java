package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.LoginDto;
import com.restaurent.RMS.dtos.request.NewPasswordRequestDto;
import com.restaurent.RMS.dtos.response.AuthenticationResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.AuthenticationService;
import com.restaurent.RMS.services.TokenService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.restaurent.RMS.dtos.request.OtpRequestDto;
import com.restaurent.RMS.dtos.request.VerifyOtpRequestDto;
import com.restaurent.RMS.dtos.response.OtpResponseDto;
import com.restaurent.RMS.entities.Token;
import com.restaurent.RMS.entities.User;
import com.restaurent.RMS.services.OptEmailServiceImpl;
import com.restaurent.RMS.services.TokenService;
import com.restaurent.RMS.services.UserServiceImpl;


import java.security.SecureRandom;

@RestController
@RequestMapping(EndpointBundle.AUTH)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;

    @PostMapping(EndpointBundle.LOGIN)
    public ResponseEntity<ResponseWrapper<AuthenticationResponseDto>> login(
            @Valid @RequestBody LoginDto request) {

        AuthenticationResponseDto responseDto = authenticationService.login(request);

        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        RestApiResponseStatusCodes.SUCCESS.getMessage(),
                        responseDto
                )
        );
    }

    @PostMapping(EndpointBundle.LOGOUT)
    public ResponseEntity<ResponseWrapper<?>> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseWrapper<>(
                            RestApiResponseStatusCodes.UNAUTHORIZED.getCode(), ValidationMessages.TOKEN_INVALID_,
                            null
                    ));
        }

        String token = authHeader.substring(7);
        tokenService.revokeToken(token);

        try {
           // tokenService.logout(token);

            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            RestApiResponseStatusCodes.SUCCESS.getCode(),
                            "Logout Successful",
                            null
                    ));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseWrapper<>(
                            RestApiResponseStatusCodes.UNAUTHORIZED.getCode(),
                            ex.getMessage(),
                            null
                    ));
        }
    }

    @PutMapping(EndpointBundle.NEW_PASSWORD)
    public ResponseEntity<ResponseWrapper<Object>> updatePassword (@Valid @RequestBody NewPasswordRequestDto newPasswordRequestDto){
        authenticationService.updatePassword(
                newPasswordRequestDto.getEmail(),
                newPasswordRequestDto.getNewPassword(),
                newPasswordRequestDto.getConfirmPassword()
        );
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        ValidationMessages.UPDATED,
                        null
                )
        );
    }

    @Autowired
   private UserServiceImpl userService;

    @Autowired
    private OptEmailServiceImpl otpEmailService;




    @PostMapping(EndpointBundle.OTP)
    public ResponseEntity<ResponseWrapper> sendOtp (@Valid @RequestBody OtpRequestDto otpRequestDto) {


        User user =  userService.getUserByEmailOrThrow(otpRequestDto.getEmail());

        //genarete otp
        SecureRandom secureRandom = new SecureRandom();
        int otpInt = secureRandom.nextInt(900000) + 100000;
        String otp = String.valueOf(otpInt);

        Token savedToken = tokenService.createTokenForUser(user, otpInt, 5);


//        otpEmailService.saveOtpForUser(user, otpInt);

//send otp to emial by using emailservice
        otpEmailService.sendOtpEmail(user.getEmail(), otp);

        OtpResponseDto response = new OtpResponseDto();
        response.setMessage("OTP sent successfully to " + user.getEmail());
        response.setExpiryTime(savedToken.getExpiresAt());


        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        RestApiResponseStatusCodes.SUCCESS.getMessage(),
                        response
                        )

        );


    }

    @PostMapping(EndpointBundle.VERIFY_OTP)
    public ResponseEntity<ResponseWrapper<?>> verifyOtp (@RequestBody VerifyOtpRequestDto request) {

        User user = userService.getUserByEmailOrThrow(request.getEmail());

        tokenService.validateotp(user, Integer.parseInt(request.getOtp()));

        tokenService.revokeOtp(user);
//        Token otpToken = tokenService.getOtpForUser(user);
//        tokenService.revokeOtp(user);
        return ResponseEntity.ok(
                new ResponseWrapper<>(RestApiResponseStatusCodes.SUCCESS.getCode(),
                        "OTP verified successfully",
                        null));
}
}
