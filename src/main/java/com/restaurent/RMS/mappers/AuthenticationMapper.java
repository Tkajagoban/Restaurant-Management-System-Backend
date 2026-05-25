package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.response.AuthenticationResponseDto;
import com.restaurent.RMS.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "expiresIn", ignore = true)
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "restaurantPrivileges", expression = "java(mapRestaurantPrivileges(user))")
    @Mapping(target = "rolePrivileges", expression = "java(mapRolePrivilegesSimple(user))")
    @Mapping(target = "rolePrivilegesMaintain", expression = "java(mapRolePrivilegesMaintain(user))")
    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "rolePrivilege", expression = "java(mapRolePrivileges(user))")
    AuthenticationResponseDto toAuthenticationResponse(User user);

    default Map<String, Integer> mapRolePrivilegesMaintain(User user) {
        if (user.getRole() == null || user.getRole().getRolePrivileges() == null) {
            return Collections.emptyMap();
        }
        return user.getRole().getRolePrivileges().stream()
                .filter(rp -> {
                    try {
                        return rp.getPrivilegeStatus() != null && "MAINTAIN".equals(rp.getPrivilegeStatus().name());
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toMap(
                        rp -> rp.getRestaurantPrivilege().getPrivilege().getName(),
                        rp -> 1,
                        (existing, replacement) -> existing));
    }

    default Map<String, AuthenticationResponseDto.RolePrivilegeDetailDto> mapRolePrivileges(User user) {
        if (user.getRole() == null || user.getRole().getRolePrivileges() == null) {
            return Collections.emptyMap();
        }
        return user.getRole().getRolePrivileges().stream()
                .collect(Collectors.toMap(
                        rp -> rp.getRestaurantPrivilege().getPrivilege().getName(),
                        rp -> AuthenticationResponseDto.RolePrivilegeDetailDto.builder()
                                .rolePrivilegeId(rp.getId())
                                .privilegeStatus(
                                        rp.getPrivilegeStatus() != null ? rp.getPrivilegeStatus().name() : null)
                                .isMaintain(rp.getPrivilegeStatus() != null
                                        && "MAINTAIN".equals(rp.getPrivilegeStatus().name()) ? 1 : 0)
                                .build(),
                        (existing, replacement) -> existing));
    }

    default Map<String, Integer> mapRolePrivilegesSimple(User user) {
        if (user.getRole() == null || user.getRole().getRolePrivileges() == null) {
            return Collections.emptyMap();
        }
        return user.getRole().getRolePrivileges().stream()
                .collect(Collectors.toMap(
                        rp -> rp.getRestaurantPrivilege().getPrivilege().getName(),
                        rp -> 1,
                        (existing, replacement) -> existing));
    }

    default Map<String, Integer> mapRestaurantPrivileges(User user) {
        if (user.getRole() == null || user.getRole().getRestaurant() == null
                || user.getRole().getRestaurant().getRestaurantPrivilege() == null) {
            return Collections.emptyMap();
        }
        return user.getRole().getRestaurant().getRestaurantPrivilege().stream()
                .filter(rp -> rp.getActive() != null && rp.getActive())
                .collect(Collectors.toMap(
                        rp -> rp.getPrivilege().getName(),
                        rp -> 1,
                        (existing, replacement) -> existing));
    }
}
