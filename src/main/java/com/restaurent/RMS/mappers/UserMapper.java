package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.request.UserRequestDto;
import com.restaurent.RMS.dtos.response.UserResponseDto;
import com.restaurent.RMS.entities.Role;
import com.restaurent.RMS.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // --- Convert Request DTO → Entity (Create/Update) ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", source = "role")
    @Mapping(target = "phoneNumber", source = "dto.phoneNumber")
    User toEntity(UserRequestDto dto, Role role);

    // (Optional) Basic mapping between User and UserResponseDto if needed
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.roleName", target = "roleName")
    UserResponseDto toDto(User user);

    @Mapping(target = "role", ignore = true)
    User toEntity(UserResponseDto userDto);

    List<User> toEntityList(List<UserResponseDto> userDtoList);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUserFromDto(UserRequestDto userRequestDto, @MappingTarget User user);
}
