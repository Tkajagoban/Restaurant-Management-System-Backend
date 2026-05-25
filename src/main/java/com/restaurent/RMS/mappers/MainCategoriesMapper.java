package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.request.MainCategoriesRequestDto;
import com.restaurent.RMS.dtos.response.MainCategoriesResponseDto;
import com.restaurent.RMS.dtos.request.RoleRequestDto;

import com.restaurent.RMS.entities.MainCategories;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
@Mapper(componentModel = "spring")
public interface MainCategoriesMapper {
    MainCategoriesRequestDto toDto(MainCategories mainCategories);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(MainCategoriesRequestDto dto, @MappingTarget MainCategories entity);

    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "subCategories", ignore = true)
    MainCategories toEntity(MainCategoriesRequestDto mainCategoriesRequestDto);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    MainCategoriesResponseDto toResponseDto(MainCategories entity);


    default Page<MainCategoriesResponseDto> toDtoPage(Page<MainCategories> page) {
        return page.map(this::toResponseDto);
    }
}
