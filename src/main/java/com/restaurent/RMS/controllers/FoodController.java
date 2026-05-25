package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.FoodRequestDto;
import com.restaurent.RMS.dtos.response.FoodResponseDto;
import com.restaurent.RMS.services.AzureImageService;
import com.restaurent.RMS.services.FoodService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.SETTINGS)
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final AzureImageService azureImageService;


    @GetMapping(EndpointBundle.FOODS)
    public ResponseEntity<ResponseWrapper<Page<FoodResponseDto>>> getAll(
            @PathVariable(required = false) Long mainCategoryId,
            @PathVariable(required = false) Long subcategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<FoodResponseDto> foodResponseDtoPage = foodService.getAllFood(mainCategoryId, subcategoryId, page, size);
        ResponseWrapper<Page<FoodResponseDto>> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.RETRIEVED,
                foodResponseDtoPage
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
    }

    @GetMapping(EndpointBundle.FOOD)
    public ResponseEntity<ResponseWrapper<Page<FoodResponseDto>>> getAllSimple(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<FoodResponseDto> foodResponseDtoPage = foodService.getAllFood(null, null, page, size);

        ResponseWrapper<Page<FoodResponseDto>> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.RETRIEVED,
                foodResponseDtoPage
        );

        return ResponseEntity.ok(responseWrapper);
    }
    @PostMapping(
            value = EndpointBundle.FOOD_ADDED,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseWrapper<FoodResponseDto>> addFood(
            @PathVariable Long mainCategoryId,
            @PathVariable Long subCategoryId,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam Boolean status
    ) {
        // Upload image to Azure and get URL
        String imageUrl = null; // ✅ FIX: declare variable

        // Upload image only if provided
        if (image != null && !image.isEmpty()) {
            imageUrl = azureImageService.uploadImage(image);
        }

        // Create DTO and set uploaded image URL
        FoodRequestDto dto = new FoodRequestDto();
        dto.setName(name);
        dto.setPrice(price);
        dto.setStatus(status);
        dto.setImage(imageUrl);  // <- THIS LINE sets the image URL

        FoodResponseDto foodResponseDto =
                foodService.createFood(mainCategoryId, subCategoryId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.CREATED.getCode(),
                        RestApiResponseStatusCodes.CREATED.getMessage(),
                        foodResponseDto
                )
        );
    }



    @DeleteMapping(EndpointBundle.FOOD_BY_ID)
    public ResponseEntity<ResponseWrapper<String>> deletefood (
            @PathVariable Long id){
        foodService.deletebyid(id);
        ResponseWrapper<String> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.DELETED.getCode(),
                ValidationMessages.DELETE_SUCCESS,
                null

        );
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping(EndpointBundle.FOOD_BY_ID)
    public ResponseEntity<ResponseWrapper<FoodResponseDto>> getfood (@PathVariable Long id){
        FoodResponseDto foodResponseDto = foodService.getfood(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.RETRIEVED,
                foodResponseDto
        ));
    }

    @PutMapping(
            value = EndpointBundle.FOOD_UPDATE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseWrapper<FoodResponseDto>> updateFood(
            @PathVariable Long id,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam Boolean status
    ) {
        String imageUrl = null;

        // Upload new image only if provided
        if (image != null && !image.isEmpty()) {
            imageUrl = azureImageService.uploadImage(image);  // <- upload and get URL
        }

        FoodRequestDto dto = new FoodRequestDto();
        dto.setName(name);
        dto.setPrice(price);
        dto.setStatus(status);
        dto.setImage(imageUrl); // can be null if no new image

        FoodResponseDto updatedFood = foodService.updateFood(dto, id);

        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.UPDATED.getCode(),
                        ValidationMessages.UPDATED,
                        updatedFood
                )
        );
    }


@GetMapping(EndpointBundle.FOOD_SEARCH)
public ResponseEntity<ResponseWrapper<List<FoodResponseDto>>> searchFoods(@RequestParam(required = false) String query){
    List<FoodResponseDto> responseDtos = foodService.searchFood(query);
    if (responseDtos.isEmpty()){
        ResponseWrapper<List<FoodResponseDto>> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                RestApiResponseStatusCodes.NOT_FOUND.getMessage(),
                null
        );
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
    } else {
        ResponseWrapper<List<FoodResponseDto>> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                responseDtos
        );
        return  ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
    }
}

}
