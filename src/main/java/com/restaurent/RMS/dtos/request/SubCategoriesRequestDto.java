package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.utils.DateAudit;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoriesRequestDto  {

    private Long id;
    @NotBlank(message = ValidationMessages.REQUIRED_DATA_MISSING)
    private String subCategoriesName;
    @Positive(message = ValidationMessages.INVALID_ID_VALUE)
    private Long mainCategoryID;
    private Boolean status;
}
