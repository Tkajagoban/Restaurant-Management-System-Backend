package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.RestaurantPrivilegeRequestDto;
import com.restaurent.RMS.dtos.response.RestaurantPrivilegeResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface RestaurantPrivilegeService {
    RestaurantPrivilegeResponseDto addRestPrivileges(Long restId, @Valid RestaurantPrivilegeRequestDto requestDto);

    Page<RestaurantPrivilegeResponseDto> getAllRestPrivileges(Long restId, int pageNo, int sizeNo);
}
