package com.restaurent.RMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoriesResponseDto {
        private Long id;
        //private Long mainCategoryID;
        private String mainCategoryName;
        private String subCategoryName;
        private Boolean status;



}

