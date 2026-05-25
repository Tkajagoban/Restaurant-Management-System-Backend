package com.restaurent.RMS.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantPrivilegeRequestDto {
    private Long privilege_id;
    private Boolean active;
}
