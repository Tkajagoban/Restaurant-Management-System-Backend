package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.RestaurantRequestDto;
//import com.restaurent.RMS.dtos.response.RestaurantResponseDto;
import com.restaurent.RMS.dtos.response.RestaurantResponseDto;
import com.restaurent.RMS.entities.Restaurant;
import com.restaurent.RMS.exceptionHandlers.AlreadyExistException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.RestaurantMapper;
import com.restaurent.RMS.repositories.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantMapper mapper;

    @Autowired
    private  CloudinaryImageService cloudinaryImageService;
    @Override
    public RestaurantResponseDto updateRestaurant(
            Long id,
            RestaurantRequestDto dto,
            MultipartFile logoImage
    ) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        // Upload image if provided
        if (logoImage != null && !logoImage.isEmpty()) {
            String imageUrl = cloudinaryImageService.uploadImage(logoImage);
            restaurant.setLogoImage(imageUrl);
        }

        restaurant.setName(dto.getName());
        restaurant.setAddress(dto.getAddress());
        restaurant.setPhoneNumber(dto.getPhoneNumber());
        restaurant.setCity(dto.getCity());
        restaurant.setWebSite(dto.getWebSite());
        restaurant.setEmail(dto.getEmail());

        restaurantRepository.save(restaurant);

        return mapper.toDto(restaurant);
    }

    @Override
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto,
                                                 MultipartFile logoImage) {

        if (restaurantRepository.count()>0){
            throw new AlreadyExistException("Restaurant already exists. You cannot add more than one.");
        }
        // Check if email already exists
        if (restaurantRepository.existsByEmail(restaurantRequestDto.getEmail())) {
            throw new AlreadyExistException("This email is already used by another restaurant.");
        }


        String logoImageUrl = null;
        if (logoImage != null && !logoImage.isEmpty()) {
            logoImageUrl = cloudinaryImageService.uploadImage(logoImage);
        }
        Restaurant restaurant = mapper.toEntity(restaurantRequestDto);
        restaurant.setLogoImage(logoImageUrl);




        // Save to database
        Restaurant saved = restaurantRepository.save(restaurant);

        // Convert to Response DTO
        return mapper.toDto(saved);
    }


    @Override
    public List<RestaurantResponseDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

    }
}