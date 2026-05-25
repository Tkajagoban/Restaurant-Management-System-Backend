package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordRequestDto {
    @NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY)
    @Email(message = ValidationMessages.INVALID_EMAIL)
    private String email;

    @NotBlank(message = ValidationMessages.PASSWORD_REQUIRED)
    @Size(min = 8, max = 25, message = ValidationMessages.PASSWORD_MIN_LENGTH)
    private String newPassword;

    @NotBlank(message = ValidationMessages.PASSWORD_REQUIRED)
    private String confirmPassword;
}
