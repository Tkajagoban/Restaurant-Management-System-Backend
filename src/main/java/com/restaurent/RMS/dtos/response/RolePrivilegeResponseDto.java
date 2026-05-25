package com.restaurent.RMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePrivilegeResponseDto {
    private String privilegeStatus;
    private Long restaurantPrivilegeId;
    private Long roleId;
    private String restaurantPrivilegeName;
}
