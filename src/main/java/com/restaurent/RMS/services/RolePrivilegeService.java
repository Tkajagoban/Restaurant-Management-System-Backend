package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.RolePrivilegeRequestDto;
import com.restaurent.RMS.dtos.response.RolePrivilegeListResponseDto;
import com.restaurent.RMS.dtos.response.RolePrivilegeResponseDto;
import org.springframework.data.domain.Page;

public interface RolePrivilegeService {
    Page<RolePrivilegeResponseDto> getAllRolePrivileges(int pageNo, int sizeNo);

    RolePrivilegeResponseDto updateRolePrivilege(Long id, RolePrivilegeRequestDto requestDto);

    RolePrivilegeListResponseDto getRolePrivilegesByRoleId(Long roleId);
}
