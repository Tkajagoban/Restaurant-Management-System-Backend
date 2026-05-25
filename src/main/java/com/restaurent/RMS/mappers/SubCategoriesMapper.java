package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.response.SubCategoriesResponseDto;
import com.restaurent.RMS.dtos.request.SubCategoriesRequestDto;
import com.restaurent.RMS.entities.SubCategories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubCategoriesMapper {
    //@Mapping(source = "mainCategories.id", target ="mainCategoriesId" )
    SubCategoriesResponseDto toDto (SubCategories subCategories);

    @Mapping(source = "name", target = "subCategoriesName")
    @Mapping(source = "mainCategories.id", target = "mainCategoryID")
    SubCategoriesRequestDto toResponse(SubCategories subCategories);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "subCategoryName")
    @Mapping(source = "status", target = "status")
    //@Mapping(source = "mainCategories.id", target = "mainCategoryId")
    @Mapping(source = "mainCategories.name", target = "mainCategoryName")


    SubCategoriesResponseDto toResponseDto(SubCategories entity);
    @Mapping(target = "mainCategories", ignore = true)
    @Mapping(source = "subCategoriesName", target = "name")
    SubCategories toSubCategoriesEntity(SubCategoriesRequestDto subCategoriesRequestDto);
}
