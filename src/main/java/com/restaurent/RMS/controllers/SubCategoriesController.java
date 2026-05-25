package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.SubCategoriesRequestDto;
import com.restaurent.RMS.dtos.response.SubCategoriesResponseDto;
import com.restaurent.RMS.dtos.request.SubCategoriesRequestDto;
import com.restaurent.RMS.entities.SubCategories;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.SubCategoriesService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointBundle.SETTINGS)
@RequiredArgsConstructor
public class SubCategoriesController {
    private final SubCategoriesService subCategoriesService;

    @PostMapping(EndpointBundle.SUB_CATEGORY_CREATE)
    public ResponseEntity<ResponseWrapper<SubCategoriesRequestDto>> createSubCategories(
            @PathVariable long mainCategoryId,
            @Valid @RequestBody SubCategoriesRequestDto SubCategoriesDto) {
        SubCategoriesDto.setMainCategoryID(mainCategoryId);
        SubCategoriesRequestDto createdSubcategories = subCategoriesService.addSubCategories(mainCategoryId, SubCategoriesDto);
        ResponseWrapper<SubCategoriesRequestDto> response =
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        RestApiResponseStatusCodes.SUCCESS.getMessage(),
                        createdSubcategories
                );
        return ResponseEntity.ok(response);
    }

    @GetMapping(EndpointBundle.SUB_CATEGORIES)
    public ResponseEntity<ResponseWrapper<List<SubCategoriesResponseDto>>> getAllSubCategories(@PathVariable Long mainCategoryId) {
        List<SubCategoriesResponseDto> subCategories = subCategoriesService.getAllSubCategories(mainCategoryId);
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        ValidationMessages.RETRIEVED,
                        subCategories
                )
        );
    }

    @PutMapping(EndpointBundle.SUB_CATEGORIES_BY_ID)
    public ResponseEntity<ResponseWrapper<SubCategoriesResponseDto>> updateSubCategories(
            @PathVariable Long id,
            @Valid @RequestBody SubCategoriesRequestDto subCategoriesRequestDto) {

        SubCategoriesResponseDto updated =
                subCategoriesService.updateSubCategories(id, subCategoriesRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        ValidationMessages.UPDATED,
                        updated
                )
        );
    }

    @DeleteMapping(EndpointBundle.SUB_CATEGORIES_BY_ID)
    public ResponseEntity<ResponseWrapper<Void>> deleteById(@PathVariable Long id) {
        subCategoriesService.subCategory_deleteById(id);
        ResponseWrapper<Void> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.DELETED.getCode(),
                ValidationMessages.DELETE_SUCCESS,
                null
        );
        return ResponseEntity.ok(response);
    }
}