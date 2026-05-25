package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.response.UserResponseDto;
import org.springframework.data.domain.Page;

import com.restaurent.RMS.dtos.request.UserRequestDto;
import com.restaurent.RMS.dtos.response.UserResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {
    UserResponseDto getUserById(Long id);
    UserResponseDto createUser(Long restaurantId,Long roleId,UserRequestDto dto);
    void User_deleteById(Long id);

    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

    Page<UserResponseDto> searchUsers(Long restaurantId, Long roleId, int page, int size);

    List<UserResponseDto> searchUser(String query);
}
