package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDto  {

    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Pattern(regexp = "^[A-Za-z]+(\\s[A-Za-z]+)*$", message = ValidationMessages.INVALID_NAME)
    private String displayName;
    @NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY)
    @Email(message = ValidationMessages.INVALID_EMAIL)
    private String sentEmail;
    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
    private String hostName;
    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Min(value = 1, message = ValidationMessages.MIN_PORT)
    @Max(value = 65535, message = ValidationMessages.MISMATCH_INPUT)
    private Long port;
    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
    private String protocol;

    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY)
    @Email(message = ValidationMessages.INVALID_EMAIL)
    private String ccMailAddress;
}