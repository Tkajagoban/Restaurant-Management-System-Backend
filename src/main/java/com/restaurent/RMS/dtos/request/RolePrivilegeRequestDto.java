package com.restaurent.RMS.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePrivilegeRequestDto {

    private String privilegeStatus;
    private Long restaurantPrivilegeId;
    private Long roleId;

}
