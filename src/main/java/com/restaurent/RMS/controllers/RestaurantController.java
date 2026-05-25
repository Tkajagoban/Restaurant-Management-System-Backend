package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.RestaurantRequestDto;
import com.restaurent.RMS.dtos.response.RestaurantResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.AzureImageService;
import com.restaurent.RMS.services.RestaurantService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(EndpointBundle.RESTAURANTS)
@RequiredArgsConstructor
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    private final AzureImageService azureImageService;

    @PostMapping(value="/added", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<RestaurantResponseDto>> createRestaurant(
            @Valid @ModelAttribute RestaurantRequestDto dto,
            @RequestParam(required = false) MultipartFile logoImage
    ) {
        // Upload image to Azure if provided
        String imageUrl = null;
        if (logoImage != null && !logoImage.isEmpty()) {
            imageUrl = azureImageService.uploadImage(logoImage);
        }


        dto.setLogoImage(imageUrl);

    RestaurantResponseDto created = restaurantService.createRestaurant(dto,logoImage);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        ValidationMessages.SAVED_SUCCESSFULL,
                        created));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseWrapper<RestaurantResponseDto>> updateRestaurant(
            @PathVariable Long id,
            @Valid @ModelAttribute RestaurantRequestDto dto,
            @RequestParam(value = "logoimage", required = false) MultipartFile logoImage
    ) {
        RestaurantResponseDto updated =
                restaurantService.updateRestaurant(id, dto, logoImage);

        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.UPDATED.getCode(),
                        ValidationMessages.UPDATED,
                        updated
                )
        );
    }





    @GetMapping
    public ResponseEntity<ResponseWrapper<List<RestaurantResponseDto>>> getAllRestaurants() {
        List<RestaurantResponseDto> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                "Restaurants fetched successfully",
                restaurants));
    }}
