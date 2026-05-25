package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.MainCategoriesRequestDto;
import com.restaurent.RMS.dtos.response.MainCategoriesResponseDto;
import com.restaurent.RMS.entities.MainCategories;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.mappers.MainCategoriesMapper;
import com.restaurent.RMS.repositories.MainCategoriesRepository;
import com.restaurent.RMS.services.MainCategoriesService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointBundle.SETTINGS)
public class MainCategoriesController {

    public final MainCategoriesService mainCategoriesService;


    @PutMapping(EndpointBundle.MAIN_CATEGORIES_BY_ID)
    public ResponseEntity<ResponseWrapper<MainCategoriesRequestDto>> updateMainCategory(@PathVariable Long id, @Valid @RequestBody MainCategoriesRequestDto requestDto) {
        MainCategoriesRequestDto updatedMainCategory = mainCategoriesService.updateMainCategory(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                RestApiResponseStatusCodes.UPDATED.getCode(),
                ValidationMessages.UPDATED,
                updatedMainCategory));
    }

    @DeleteMapping(EndpointBundle.MAIN_CATEGORIES_BY_ID)
    public ResponseEntity<ResponseWrapper<Void>> deleteById(@PathVariable Long id) {

        mainCategoriesService.mainCategory_deleteById(id);

        ResponseWrapper<Void> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.DELETE_SUCCESS,
                null
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping(EndpointBundle.MAIN_CATEGORIES)
    public ResponseEntity<ResponseWrapper<Page<MainCategoriesResponseDto>>> getAllMain(
            @RequestParam(required = false)Integer page,
            @RequestParam(required = false)Integer size) {

        Page<MainCategoriesResponseDto> mainCategories = mainCategoriesService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        ValidationMessages.RETRIEVED,
                        mainCategories
                )
        );
    }

    //Kanujan create main categories
    @PostMapping(EndpointBundle.MAIN_CATEGORIES_CREATE)
    public ResponseEntity<ResponseWrapper<MainCategoriesResponseDto>> createMainCategory(@PathVariable Long restaurantId, @Valid @RequestBody MainCategoriesRequestDto mainCategoriesRequestDto){
        MainCategoriesResponseDto createMainCategory = mainCategoriesService.createMainCategory(restaurantId,mainCategoriesRequestDto);
        if (createMainCategory != null){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.SAVED_SUCCESSFULL,
                    createMainCategory
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    ValidationMessages.RESOURCE_NOT_FOUND,
                    null
            ));
        }
    }

    @GetMapping(EndpointBundle.MAIN_CATEGORIES_BY_ID)
    public ResponseEntity<ResponseWrapper<MainCategoriesResponseDto>> getbyid(@PathVariable Long id) {
        MainCategoriesResponseDto mainCategoriesResponseDto=mainCategoriesService.getMainCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.RETRIEVED,
                mainCategoriesResponseDto

        ));

    }
}
