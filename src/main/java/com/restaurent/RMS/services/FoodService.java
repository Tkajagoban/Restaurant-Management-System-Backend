package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.FoodRequestDto;
import com.restaurent.RMS.dtos.response.FoodResponseDto;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;

import com.restaurent.RMS.dtos.request.FoodRequestDto;
import com.restaurent.RMS.dtos.response.FoodResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {
    void deletebyid(Long id);
    FoodResponseDto createFood(Long mainCategoriesId, Long subCategoriesId, @Valid FoodRequestDto foodRequestDto);
    Page<FoodResponseDto> getAllFood(Long mainCategoryId, Long subcategoryId, int page, int size);

    FoodResponseDto getfood(Long id);
    FoodResponseDto updateFood(FoodRequestDto foodRequestDto, Long id);

    List<FoodResponseDto> searchFood(String query);
}
