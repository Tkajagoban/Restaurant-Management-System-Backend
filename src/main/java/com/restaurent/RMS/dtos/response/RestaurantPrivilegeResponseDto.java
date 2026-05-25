package com.restaurent.RMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantPrivilegeResponseDto {
    private Long id;
    private Long privilege_id;
    private String privilege_name;
    private Long restaurant_id;
}
