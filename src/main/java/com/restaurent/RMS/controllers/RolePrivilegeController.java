package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.RolePrivilegeRequestDto;
import com.restaurent.RMS.dtos.response.RolePrivilegeListResponseDto;
import com.restaurent.RMS.dtos.response.RolePrivilegeResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.RolePrivilegeService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointBundle.SETTINGS)
@RequiredArgsConstructor
public class RolePrivilegeController {
    private final RolePrivilegeService rolePrivilegeService;

    @GetMapping(EndpointBundle.ROLE_PRIVILEGE_GET_ALL)
    public ResponseEntity<ResponseWrapper<Page<RolePrivilegeResponseDto>>> getAllRolePrivilege(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        int pageNo = (page == null) ? 0 : page;
        int sizeNo = (size == null) ? 100 : size;
        Page<RolePrivilegeResponseDto> responseDtos = rolePrivilegeService.getAllRolePrivileges(pageNo, sizeNo);

        if (responseDtos.isEmpty()) {
            ResponseWrapper<Page<RolePrivilegeResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    RestApiResponseStatusCodes.NOT_FOUND.getMessage(),
                    responseDtos);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        } else {
            ResponseWrapper<Page<RolePrivilegeResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                    responseDtos);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
    }

    @PutMapping(EndpointBundle.ROLE_PRIVILEGE)
    public ResponseEntity<ResponseWrapper<RolePrivilegeResponseDto>> updateRolePrivilege(
            @RequestParam(required = false) Long id,
            @RequestBody RolePrivilegeRequestDto requestDto) {
        RolePrivilegeResponseDto responseDto = rolePrivilegeService.updateRolePrivilege(id, requestDto);

        if (responseDto == null) {
            ResponseWrapper<RolePrivilegeResponseDto> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.BAD_REQUEST.getCode(),
                    RestApiResponseStatusCodes.BAD_REQUEST.getMessage(),
                    responseDto);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        } else {
            ResponseWrapper<RolePrivilegeResponseDto> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.UPDATED.getCode(),
                    RestApiResponseStatusCodes.UPDATED.getMessage(),
                    responseDto);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }

    }

    @GetMapping(EndpointBundle.ROLE_PRIVILEGE_BY_ROLE_ID)
    public ResponseEntity<ResponseWrapper<RolePrivilegeListResponseDto>> getRolePrivilegesByRoleId(
            @PathVariable Long roleId) {
        RolePrivilegeListResponseDto responseDto = rolePrivilegeService.getRolePrivilegesByRoleId(roleId);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                responseDto));
    }
}
