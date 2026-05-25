package com.restaurent.RMS.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodResponseDto {
    private Long id;

    private String name;
    private String image;
    private Double price;
    private Boolean status;
    private Long mainCategoryId;
    private Long subcategoryId;
    private String mainCategoryName;
    private String subCategoryName;
}
