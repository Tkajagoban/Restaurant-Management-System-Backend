package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.RestaurantRequestDto;
import com.restaurent.RMS.dtos.response.RestaurantResponseDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//import com.restaurent.RMS.dtos.response.RestaurantResponseDto;

@Service
public interface RestaurantService {
    RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto, MultipartFile logoImage);

    List<RestaurantResponseDto> getAllRestaurants();

    RestaurantResponseDto updateRestaurant(Long id, @Valid RestaurantRequestDto dto, MultipartFile logoImage);
}