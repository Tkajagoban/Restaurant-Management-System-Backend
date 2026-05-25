package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotEmpty(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Pattern(regexp = "^[A-Za-z]+$", message = ValidationMessages.INVALID_NAME)
    private String firstName;
    @NotEmpty(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Pattern(regexp = "^[A-Za-z]+$", message = ValidationMessages.INVALID_NAME)
    private String lastName;
    @NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY)
    @Email(message = ValidationMessages.INVALID_EMAIL)
    private String email;
    @NotBlank(message = ValidationMessages.MISMATCH_INPUT)
    @Pattern(regexp = "^\\d{9}[VvXx]|\\d{12}$", message = ValidationMessages.MISMATCH_INPUT)
    private String nic;
    @NotEmpty(message = ValidationMessages.REQUIRED_DATA_MISSING)
    private String address;
    @NotEmpty(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Pattern(regexp = "^[A-Za-z ]+$", message = ValidationMessages.INVALID_NAME)
    private String city;
    @Positive(message = ValidationMessages.INVALID_ID_VALUE)
    private Long roleId;
    @Positive(message = ValidationMessages.INVALID_ID_VALUE)
    private Long restaurantId;
    @Pattern(regexp = "^(0\\d{9}|\\+\\d{1,3}\\d{4,14})$", message = ValidationMessages.MISMATCH_INPUT)
    private String phoneNumber;
}
