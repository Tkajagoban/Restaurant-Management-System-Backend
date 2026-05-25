package com.restaurent.RMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {
    private String accessToken;
    private Long expiresIn;
    private Long userId;
    private java.util.Map<String, Integer> restaurantPrivileges;
    private java.util.Map<String, Integer> rolePrivileges;
    private java.util.Map<String, Integer> rolePrivilegesMaintain;
    private Long roleId;
    private java.util.Map<String, RolePrivilegeDetailDto> rolePrivilege;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RolePrivilegeDetailDto {
        private Long rolePrivilegeId;
        private String privilegeStatus;
        private Integer isMaintain; // 1 if Status is MAINTAIN, 0 otherwise
    }
}
