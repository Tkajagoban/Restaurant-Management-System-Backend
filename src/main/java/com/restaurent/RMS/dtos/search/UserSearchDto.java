package com.restaurent.RMS.dtos.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
    private int page = 0;
    private int size = 10;
    private Long restaurantId;
    private Long roleId; // optional
}
