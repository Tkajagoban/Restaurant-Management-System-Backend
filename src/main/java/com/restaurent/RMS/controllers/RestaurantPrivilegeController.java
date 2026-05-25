package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.RestaurantPrivilegeRequestDto;
import com.restaurent.RMS.dtos.response.RestaurantPrivilegeResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.RestaurantPrivilegeService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointBundle.SETTINGS)
@RequiredArgsConstructor
public class RestaurantPrivilegeController {
    private final RestaurantPrivilegeService restaurantPrivilegeService;

    @PostMapping(EndpointBundle.RESTAURANT_PRIVILEGE_ADDED)
    public ResponseEntity<ResponseWrapper<RestaurantPrivilegeResponseDto>> addRestPrivilege(
            @PathVariable("restaurantId") Long restId,
            @Valid @RequestBody RestaurantPrivilegeRequestDto requestDto) {
        RestaurantPrivilegeResponseDto responseDto = restaurantPrivilegeService.addRestPrivileges(restId, requestDto);

        ResponseWrapper<RestaurantPrivilegeResponseDto> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.CREATED.getCode(),
                RestApiResponseStatusCodes.CREATED.getMessage(),
                responseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
    }

    @GetMapping(EndpointBundle.RESTAURANT_PRIVILEGE_GETALL)
    public ResponseEntity<ResponseWrapper<Page<RestaurantPrivilegeResponseDto>>> getAllRestaurantPrivilege(
            @PathVariable("restaurantId") Long restId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        int pageNo = (page == null) ? 0 : page;
        int sizeNo = (size == null) ? 100 : size;

        Page<RestaurantPrivilegeResponseDto> responseDtos = restaurantPrivilegeService.getAllRestPrivileges(restId,
                pageNo, sizeNo);
        if (responseDtos.isEmpty()) {
            ResponseWrapper<Page<RestaurantPrivilegeResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    RestApiResponseStatusCodes.NOT_FOUND.getMessage(),
                    responseDtos);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        } else {
            ResponseWrapper<Page<RestaurantPrivilegeResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                    responseDtos);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
    }
}
