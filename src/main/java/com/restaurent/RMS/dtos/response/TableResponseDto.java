package com.restaurent.RMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableResponseDto {
    private Long id;
    private String tableNumber;
    private Integer guestCount;
    private Boolean status;
}
