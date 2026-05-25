package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRequestDto {
    @NotEmpty(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Size(min = 2, max = 100, message = ValidationMessages.NAME_SIZE)
    @Pattern(regexp = "^[^ ].*$", message = ValidationMessages.NAME_LEADING_SPACE)
    @Pattern(regexp = "^.*[^ ]$", message = ValidationMessages.NAME_TRAILING_SPACE)
    @Pattern(regexp = "^[A-Za-z ]+$", message = ValidationMessages.INVALID_NAME)
    private String name;
    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @DecimalMin(value = "0.00", message = ValidationMessages.INVALID_INPUT)
    @DecimalMax(value = "100.00", message = ValidationMessages.INVALID_INPUT)
    private Double percentage;
    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)
    private Boolean status;
}
