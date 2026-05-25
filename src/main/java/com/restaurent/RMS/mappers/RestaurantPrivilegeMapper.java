package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.request.RestaurantPrivilegeRequestDto;
import com.restaurent.RMS.dtos.response.RestaurantPrivilegeResponseDto;
import com.restaurent.RMS.entities.RestaurantPrivilege;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantPrivilegeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant.id", source = "restId")
    @Mapping(target = "privilege.id", source = "requestDto.privilege_id")
    @Mapping(target = "active", source = "requestDto.active")
    RestaurantPrivilege toEntity(Long restId, RestaurantPrivilegeRequestDto requestDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "privilege_id", source = "privilege.id")
    @Mapping(target = "privilege_name", source = "privilege.name")
    @Mapping(target = "restaurant_id", source = "restaurant.id")
    RestaurantPrivilegeResponseDto toDto(RestaurantPrivilege restaurantPrivilege);
}
