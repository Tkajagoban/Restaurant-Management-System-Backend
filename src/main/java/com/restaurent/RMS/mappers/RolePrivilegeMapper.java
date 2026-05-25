package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.request.RolePrivilegeRequestDto;
import com.restaurent.RMS.dtos.response.RolePrivilegeResponseDto;
import com.restaurent.RMS.entities.RolePrivilege;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolePrivilegeMapper {

    @Mapping(target = "privilegeStatus", source = "privilegeStatus")
    @Mapping(target = "role.id", source = "roleId")
    @Mapping(target = "restaurantPrivilege.id", source = "restaurantPrivilegeId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    RolePrivilege toEntity(RolePrivilegeRequestDto rolePrivilegeRequestDto);

    @Mapping(target = "privilegeStatus", source = "privilegeStatus")
    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "restaurantPrivilegeId", source = "restaurantPrivilege.id")
    @Mapping(target = "restaurantPrivilegeName", source = "restaurantPrivilege.privilege.name")
    RolePrivilegeResponseDto toDto(RolePrivilege rolePrivilege);
}
