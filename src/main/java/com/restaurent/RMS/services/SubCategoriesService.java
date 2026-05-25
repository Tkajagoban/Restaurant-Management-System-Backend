package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.SubCategoriesRequestDto;
import com.restaurent.RMS.dtos.response.SubCategoriesResponseDto;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface SubCategoriesService {
    SubCategoriesRequestDto addSubCategories(Long mainCategoryId, SubCategoriesRequestDto subCategoriesRequestDto);
    List<SubCategoriesResponseDto> getAllSubCategories(Long mainCategoryId);
  //  List<SubCategoriesResponseDto> getSubCategoriesByMainCategoryId(Long mainCategoryId);
    SubCategoriesResponseDto updateSubCategories(Long id, SubCategoriesRequestDto subCategoriesRequestDto);
    void subCategory_deleteById(Long id);
}
