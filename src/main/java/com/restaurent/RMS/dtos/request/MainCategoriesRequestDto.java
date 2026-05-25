package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainCategoriesRequestDto {
    @NotBlank(message = ValidationMessages.INVALID_NAME)
    @Size(min = 2, max = 100, message = ValidationMessages.NAME_SIZE)
    @Pattern(regexp = "^[^ ].*$", message = ValidationMessages.NAME_LEADING_SPACE)
    @Pattern(regexp = "^.*[^ ]$", message = ValidationMessages.NAME_TRAILING_SPACE)
    @Pattern(regexp = "^[A-Za-z ]+$", message = ValidationMessages.INVALID_NAME)
    private String name;
    @NotNull(message = ValidationMessages.STATUS)
    private Boolean status;
    @Positive(message = ValidationMessages.INVALID_ID_VALUE)
    private Long restaurantId;

}