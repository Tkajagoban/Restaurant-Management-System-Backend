package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotEmpty(message = ValidationMessages.EMAIL_NOT_EMPTY)
    @Email(message = ValidationMessages.INVALID_EMAIL)
    private String email;
    @NotEmpty(message = ValidationMessages.PASSWORD_NOT_EMPTY)
    @Size(min = 8, message = ValidationMessages.PASSWORD_MIN_LENGTH)
    private String password;
}
