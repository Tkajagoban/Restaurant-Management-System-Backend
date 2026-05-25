package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class  OtpRequestDto {
    @NotBlank(message = ValidationMessages.EMAIL_NOT_EMPTY)
    @Email(message = ValidationMessages.MISMATCH_INPUT)
    private String email;


}
