package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.RestaurantPrivilegeRequestDto;
import com.restaurent.RMS.dtos.response.RestaurantPrivilegeResponseDto;
import com.restaurent.RMS.entities.Privilege;
import com.restaurent.RMS.entities.Restaurant;
import com.restaurent.RMS.entities.RestaurantPrivilege;
import com.restaurent.RMS.exceptionHandlers.BusinessRuleViolationException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.RestaurantPrivilegeMapper;
import com.restaurent.RMS.repositories.PrivilegeRepository;
import com.restaurent.RMS.repositories.RestaurantPrivilegeRepository;
import com.restaurent.RMS.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantPrivilegeServiceImpl implements RestaurantPrivilegeService {
        private final RestaurantPrivilegeRepository restaurantPrivilegeRepository;
        private final RestaurantPrivilegeMapper restaurantPrivilegeMapper;
        private final RestaurantRepository restaurantRepository;
        private final PrivilegeRepository privilegeRepository;

        @Override
        public RestaurantPrivilegeResponseDto addRestPrivileges(Long restId, RestaurantPrivilegeRequestDto requestDto) {
                Restaurant restaurant = restaurantRepository.findById(restId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Restaurant not found with id: " + restId));

                Privilege privilege = privilegeRepository.findById(requestDto.getPrivilege_id())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Privilege not found with id: " + requestDto.getPrivilege_id()));

                restaurantPrivilegeRepository.findByPrivilegeIdAndRestaurantId(privilege.getId(), restaurant.getId())
                                .ifPresent(existing -> {
                                        throw new BusinessRuleViolationException(
                                                        "Privilege already assigned to this restaurant");
                                });

                RestaurantPrivilege restaurantPrivilege = restaurantPrivilegeMapper.toEntity(restId, requestDto);

                RestaurantPrivilege saved = restaurantPrivilegeRepository.save(restaurantPrivilege);

                return restaurantPrivilegeMapper.toDto(saved);
        }

        @Override
        public Page<RestaurantPrivilegeResponseDto> getAllRestPrivileges(Long restId, int pageNo, int sizeNo) {
                Pageable pageable;
                if (pageNo < 0 || sizeNo <= 0) {
                        throw new IllegalArgumentException("Page must be >= 0 and size must be > 0.");
                }
                pageable = PageRequest.of(pageNo, sizeNo);
                Page<RestaurantPrivilege> restaurantPrivilegePage = restaurantPrivilegeRepository
                                .findAllByRestaurantIdAndActiveTrue(restId, pageable);

                if (pageNo >= restaurantPrivilegePage.getTotalPages() && restaurantPrivilegePage.getTotalPages() > 0) {
                        throw new ResourceNotFoundException(
                                        "Page " + pageNo + " not found. Total pages: "
                                                        + restaurantPrivilegePage.getTotalPages());
                }
                return restaurantPrivilegePage.map(restaurantPrivilegeMapper::toDto);
        }
}
