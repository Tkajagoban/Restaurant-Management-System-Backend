package com.restaurent.RMS.dtos.response;

import com.restaurent.RMS.entities.Restaurant;
import com.restaurent.RMS.entities.SubCategories;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainCategoriesResponseDto {
    private Long id;
    private String name;
    private Boolean status;
    private Long restaurantId;

}
