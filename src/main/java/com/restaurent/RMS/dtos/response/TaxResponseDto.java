package com.restaurent.RMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxResponseDto {
    private Long id;
    private String name;
    private Double percentage;
    private Boolean status;
}
