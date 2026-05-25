package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.RoleRequestDto;
import com.restaurent.RMS.dtos.response.RoleResponseDto;
import com.restaurent.RMS.entities.Restaurant;
import com.restaurent.RMS.mappers.RoleMapper;
import com.restaurent.RMS.entities.Role;
import com.restaurent.RMS.exceptionHandlers.AlreadyExistException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.specification.RoleSpecs;
import com.restaurent.RMS.utils.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.restaurent.RMS.repositories.RoleRepository;
import com.restaurent.RMS.repositories.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElse(null);
    }

    @Override
    public RoleRequestDto addRole(Long restaurantId, RoleRequestDto roleRequestDto) {

        String roleName = roleRequestDto.getRoleName();

        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be empty.");
        }

        if (!roleName.equals(roleName.trim())) {
            throw new IllegalArgumentException("Role name cannot start or end with a space.");
        }

        if (!roleName.matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
            throw new IllegalArgumentException("Role name must contain only letters (A–Z).");
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));

        validateDuplicateRole(roleName, restaurantId, null);

        Role role = roleMapper.toRoleEntity(roleRequestDto);
        role.setRestaurant(restaurant);

        Role res = roleRepository.save(role);
        RoleRequestDto response = roleMapper.toResponse(res);
        response.setRestaurantId(restaurantId);

        return response;
    }

    private void validateDuplicateRole(String roleName, Long restaurantId, Long excludeId) {
        List<Role> restaurantRoles = roleRepository.findByRestaurantId(restaurantId);
        String normalizedName = roleName.toLowerCase().replace(" ", "");

        for (Role role : restaurantRoles) {
            if (excludeId != null && role.getId().equals(excludeId)) {
                continue;
            }
            String existingNormalizedName = role.getRoleName().toLowerCase().replace(" ", "");
            if (existingNormalizedName.equals(normalizedName)) {
                throw new AlreadyExistException("Already exists");
            }
        }
    }

    @Override
    public List<RoleRequestDto> getAllRoles(long restaurantId) {

        List<Role> roles = roleRepository.findByRestaurantId(restaurantId);

        if (roles == null || roles.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No roles found for restaurant ID: " + restaurantId);
        }

        List<RoleRequestDto> res = roles.stream()
                .map(role -> {
                    RoleRequestDto dto = roleMapper.toResponse(role);
                    dto.setCreateAt(role.getCreateAt());
                    dto.setUpdateAt(role.getUpdateAt());
                    return dto;
                })
                .collect(Collectors.toList());
        return res;
    }

    @Override
    public RoleRequestDto getrolebyid(Long id) {

        if (id < 0) {
            throw new IllegalArgumentException(ValidationMessages.CONSTRAINT_VIOLATION);

        }
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ValidationMessages.RESOURCE_NOT_FOUND));

        RoleRequestDto dto = new RoleRequestDto();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        dto.setCreateAt(role.getCreateAt());
        dto.setUpdateAt(role.getUpdateAt());

        if (role.getRestaurant() != null) {
            dto.setRestaurantId(role.getRestaurant().getId());
        }

        return dto;
    }

    @Override
    public void deletebyid(Long id) {
        roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role id not found: " + id));

        roleRepository.deleteById(id);
    }

    @Override
    public RoleRequestDto updateRole(RoleRequestDto roleRequestDto, Long id) {

        String roleName = roleRequestDto.getRoleName();

        // 1. Role must exist
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role ID not found: " + id));

        // 2. Restaurant must exist
        if (roleRequestDto.getRestaurantId() == null) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }

        Restaurant restaurant = restaurantRepository.findById(roleRequestDto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Role name is required.");
        }

        if (!roleName.equals(roleName.trim())) {
            throw new IllegalArgumentException("Role name cannot start or end with a space.");
        }

        if (!roleName.matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
            throw new IllegalArgumentException("Role name can contain only letters and single spaces between words.");
        }

        validateDuplicateRole(roleName, roleRequestDto.getRestaurantId(), id);

        role.setRoleName(roleName);
        role.setRestaurant(restaurant);

        Role updated = roleRepository.save(role);

        RoleRequestDto res = roleMapper.toResponse(updated);
        res.setRestaurantId(roleRequestDto.getRestaurantId());
        return res;

    }

    @Override
    public List<RoleResponseDto> searchRole(String query) {
        List<Role> roles = roleRepository.findAll(RoleSpecs.search(query));
        return roles.stream()
                .map(roleMapper::toResponseDto)
                .toList();
    }

}
