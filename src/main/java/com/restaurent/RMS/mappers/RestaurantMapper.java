package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.request.RestaurantRequestDto;
import com.restaurent.RMS.dtos.response.RestaurantResponseDto;
import com.restaurent.RMS.entities.Restaurant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;




@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

    @Mapping(target = "logoImage", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "address", source = "dto.address")
    @Mapping(target = "email", source = "dto.email")
    Restaurant toEntity(RestaurantRequestDto dto);

    RestaurantResponseDto toDto (Restaurant entity);


}