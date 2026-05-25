package com.restaurent.RMS.dtos.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpRequestDto {
    @NotBlank
        @Email
        private String email;

        @NotBlank
        private String otp;

}
