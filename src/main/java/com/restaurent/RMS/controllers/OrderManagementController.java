package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.response.OrderManagementDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.OrderManagementService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.SETTINGS)
@RequiredArgsConstructor
public class OrderManagementController {
    private final OrderManagementService orderManagementService;

    @GetMapping(EndpointBundle.ORDER_MANAGEMENT_SEARCH)
    public ResponseEntity<ResponseWrapper<List<OrderManagementDto>>> searchOrderManagement(
            @RequestParam String query) {
        List<OrderManagementDto> responseDtos = orderManagementService.searchOrderManagement(query);
        if (responseDtos.isEmpty()) {
            ResponseWrapper<List<OrderManagementDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    RestApiResponseStatusCodes.NOT_FOUND.getMessage(),
                    responseDtos);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        } else {
            ResponseWrapper<List<OrderManagementDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                    responseDtos);
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
    }
}
