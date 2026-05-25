package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.response.FoodResponseDto;
import com.restaurent.RMS.entities.Food;
import com.restaurent.RMS.dtos.request.FoodRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "mainCategories", ignore = true)
    @Mapping(target = "subCategories", ignore = true)
    Food toEntity(FoodRequestDto dto);


    @Mapping(target = "mainCategoryId", source = "mainCategories.id")
    @Mapping(target = "subcategoryId", source = "subCategories.id")
    @Mapping(target = "mainCategoryName", source = "mainCategories.name")
    @Mapping(target = "subCategoryName", source = "subCategories.name")

    FoodResponseDto toDto(Food food);
    FoodResponseDto toFoodResponseDto(Food food);

}
