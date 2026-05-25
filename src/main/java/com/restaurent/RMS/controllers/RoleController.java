package com.restaurent.RMS.controllers;


import com.restaurent.RMS.dtos.request.RoleRequestDto;
import com.restaurent.RMS.dtos.response.RoleResponseDto;
import com.restaurent.RMS.dtos.response.TableResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.RoleService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(EndpointBundle.SETTINGS)

public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(EndpointBundle.ROLE_BY_ID)

    public ResponseEntity<ResponseWrapper<RoleRequestDto>> getrolebyid(@PathVariable Long id) {
        RoleRequestDto roleResponseDto = roleService.getrolebyid(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.RETRIEVED,
                roleResponseDto
        ));

    }

    @PostMapping(EndpointBundle.ROLES_CREATE)
    public ResponseEntity<ResponseWrapper<RoleRequestDto>> createRole(
            @PathVariable long restaurantId,
            @RequestBody RoleRequestDto roleDto) {

          roleDto.setRestaurantId(restaurantId);

        RoleRequestDto createdRole = roleService.addRole(restaurantId,roleDto);

        ResponseWrapper<RoleRequestDto> response =
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        RestApiResponseStatusCodes.SUCCESS.getMessage(),
                        createdRole
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(EndpointBundle.ROLE_BY_ID)
    public ResponseEntity<ResponseWrapper<String>> deletebyId(@PathVariable Long id) {
        roleService.deletebyid(id);
        ResponseWrapper<String> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.DELETED.getCode(),
                ValidationMessages.DELETE_SUCCESS,
                null

        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(EndpointBundle.ROLES)
    public ResponseEntity<ResponseWrapper<List<RoleRequestDto>>> getAllRoles(
            @PathVariable long restaurantId) {

        List<RoleRequestDto> roles = roleService.getAllRoles(restaurantId);

        ResponseWrapper<List<RoleRequestDto>> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.RETRIEVED,
                roles
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping(EndpointBundle.ROLE_BY_ID)
    public ResponseEntity<ResponseWrapper<RoleRequestDto>> updateRole(
            @RequestBody RoleRequestDto roleRequestDto,
            @PathVariable Long id) {

        RoleRequestDto updated = roleService.updateRole(roleRequestDto, id);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.UPDATED,
                updated
        ));
    }

    @GetMapping(EndpointBundle.ROLE_SEARCH)
    public ResponseEntity<ResponseWrapper<List<RoleResponseDto>>> searchRoles(@RequestParam(required = false) String query) {
        List<RoleResponseDto> roleResponseDto = roleService.searchRole(query);
        if (roleResponseDto.isEmpty()){
            ResponseWrapper<List<RoleResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    RestApiResponseStatusCodes.NOT_FOUND.getMessage(),
                    null
            );
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        } else {
            ResponseWrapper<List<RoleResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                    roleResponseDto
            );
            return  ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
    }
}
