package com.restaurent.RMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolePrivilegeListResponseDto {
    private Long roleId;
    private Map<String, RolePrivilegeDetailDto> rolePrivileges;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RolePrivilegeDetailDto {
        private Long rolePrivilegeId;
        private Long restaurantPrivilegeId;
        private String privilegeStatus;
    }
}
