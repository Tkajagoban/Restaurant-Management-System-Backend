package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.RoleRequestDto;
import com.restaurent.RMS.dtos.response.RoleResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface RoleService {
    RoleRequestDto addRole(Long restaurantId,RoleRequestDto roleRequestDto);

    RoleRequestDto getrolebyid(Long id);

    void deletebyid(Long id);
    List<RoleRequestDto> getAllRoles(long restaurantId);

    RoleRequestDto updateRole(RoleRequestDto roleRequestDto, Long id);

    List<RoleResponseDto> searchRole(String query);
}
