package com.restaurent.RMS.mappers;


import com.restaurent.RMS.dtos.request.RestaurantPrivilegeRequestDto;
import com.restaurent.RMS.dtos.response.PrivilegeResponseDto;
import com.restaurent.RMS.entities.Privilege;
import com.restaurent.RMS.repositories.PrivilegeRepository;
import org.mapstruct.Mapper;

@Mapper
public interface PrivilegeMapper {
    Privilege toEntity(RestaurantPrivilegeRequestDto requestDto);
    PrivilegeResponseDto toDto(Privilege privilege);
}
