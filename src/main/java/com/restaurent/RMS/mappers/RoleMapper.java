package com.restaurent.RMS.mappers;
import com.restaurent.RMS.dtos.request.RoleRequestDto;
import com.restaurent.RMS.dtos.response.RoleResponseDto;
import com.restaurent.RMS.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "restaurant.id", target = "restaurantId")
    RoleRequestDto toResponse(Role role);
    Role toRoleEntity(RoleRequestDto roleRequestDto);
    RoleResponseDto toResponseDto(Role role);

}
