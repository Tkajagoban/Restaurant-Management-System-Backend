package com.restaurent.RMS.services;
import com.restaurent.RMS.dtos.request.MainCategoriesRequestDto;
import com.restaurent.RMS.dtos.response.MainCategoriesResponseDto;
import com.restaurent.RMS.entities.MainCategories;
import com.restaurent.RMS.dtos.response.MainCategoriesResponseDto;
import org.springframework.data.domain.Page;

public interface MainCategoriesService {

    MainCategoriesRequestDto updateMainCategory(Long id, MainCategoriesRequestDto requestDto);
    Page<MainCategoriesResponseDto> getAll(Integer page, Integer size);

    void mainCategory_deleteById(Long id);
    MainCategoriesResponseDto createMainCategory(Long restaurantId, MainCategoriesRequestDto mainCategoriesRequestDto);
    MainCategoriesResponseDto  getMainCategory(Long id );
}
