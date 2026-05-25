package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.response.TableResponseDto;
import com.restaurent.RMS.entities.restaurantTable;
import com.restaurent.RMS.dtos.request.TableRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TableMapper {


    TableResponseDto toResponseDto(restaurantTable entity);
    TableResponseDto toDto(restaurantTable restaurantTable);

    TableMapper INSTANCE = Mappers.getMapper(TableMapper.class);

    restaurantTable toEntity(TableRequestDto dto);

    TableRequestDto toDtos(restaurantTable table);
}
