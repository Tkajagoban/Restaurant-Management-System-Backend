package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableRequestDto{
    private Long id;
    @NotEmpty(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Pattern(regexp = "^[A-Z0-9]+$", message = ValidationMessages.INVALID_INPUT)

    private String tableNumber;
    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)
    @Positive(message = ValidationMessages.INVALID_INPUT)
    private Integer guestCount;
    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)
    private Boolean status;
}
