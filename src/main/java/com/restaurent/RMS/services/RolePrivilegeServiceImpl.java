package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.RolePrivilegeRequestDto;
import com.restaurent.RMS.dtos.response.RolePrivilegeListResponseDto;
import com.restaurent.RMS.dtos.response.RolePrivilegeResponseDto;
import com.restaurent.RMS.entities.RestaurantPrivilege;
import com.restaurent.RMS.entities.Role;
import com.restaurent.RMS.entities.RolePrivilege;
import com.restaurent.RMS.enums.PrivilegeStatus;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.exceptionHandlers.UnauthorizedException;
import com.restaurent.RMS.mappers.RolePrivilegeMapper;
import com.restaurent.RMS.repositories.RestaurantPrivilegeRepository;
import com.restaurent.RMS.repositories.RolePrivilegeRepository;
import com.restaurent.RMS.repositories.RoleRepository;
import com.restaurent.RMS.utils.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RolePrivilegeServiceImpl implements RolePrivilegeService {
        private final RolePrivilegeRepository rolePrivilegeRepository;
        private final RolePrivilegeMapper rolePrivilegeMapper;
        private final RoleRepository roleRepository;
        private final RestaurantPrivilegeRepository restaurantPrivilegeRepository;

        @Override
        public Page<RolePrivilegeResponseDto> getAllRolePrivileges(int pageNo, int sizeNo) {
                Pageable pageable;
                if (pageNo < 0 || sizeNo <= 0) {
                        throw new IllegalArgumentException("Page must be >= 0 and size must be > 0.");
                }
                pageable = PageRequest.of(pageNo, sizeNo);
                Page<RolePrivilege> rolePrivilegePage = rolePrivilegeRepository.findAll(pageable);
                if (rolePrivilegePage.isEmpty()) {
                        throw new ResourceNotFoundException("Not find restaurant id");
                }
                if (pageNo >= rolePrivilegePage.getTotalPages() && rolePrivilegePage.getTotalPages() > 0) {
                        throw new ResourceNotFoundException(
                                        "Page " + pageNo + " not found. Total pages: "
                                                        + rolePrivilegePage.getTotalPages());
                }

                return rolePrivilegePage.map(rolePrivilegeMapper::toDto);
        }

        @Override
        @Transactional
        public RolePrivilegeResponseDto updateRolePrivilege(Long id, RolePrivilegeRequestDto requestDto) {
                // Security Check: Ensure user has Role Privileges:MAINTAIN status
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                boolean hasMaintainAccess = authentication.getAuthorities().stream()
                                .anyMatch(a -> "Role Privileges:MAINTAIN".equals(a.getAuthority()));

                if (!hasMaintainAccess) {
                        throw new UnauthorizedException(ValidationMessages.NOT_ACCESS);
                }

                // Determine if we should update an existing record by Role/Privilege pair
                // (Logic Identity)
                RolePrivilege rolePrivilegeToUpdate = rolePrivilegeRepository.findByRole_IdAndRestaurantPrivilege_Id(
                                requestDto.getRoleId(), requestDto.getRestaurantPrivilegeId())
                                .orElse(null);

                if (rolePrivilegeToUpdate == null) {
                        // If status is NONE and no record exists, nothing to do
                        if ("NONE".equals(requestDto.getPrivilegeStatus())) {
                                return new RolePrivilegeResponseDto(); // Or handle as empty
                        }

                        // Create new record
                        rolePrivilegeToUpdate = rolePrivilegeMapper.toEntity(requestDto);

                        // Explicitly validate Role and RestaurantPrivilege for new record and set them
                        Role role = roleRepository.findById(requestDto.getRoleId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Role not found with id: " + requestDto.getRoleId()));
                        RestaurantPrivilege rp = restaurantPrivilegeRepository
                                        .findById(requestDto.getRestaurantPrivilegeId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "RestaurantPrivilege not found with id: "
                                                                        + requestDto.getRestaurantPrivilegeId()));

                        rolePrivilegeToUpdate.setRole(role);
                        rolePrivilegeToUpdate.setRestaurantPrivilege(rp);
                } else {
                        // Update existing record's status
                        if (requestDto.getPrivilegeStatus() != null) {
                                if ("NONE".equals(requestDto.getPrivilegeStatus())) {
                                        rolePrivilegeRepository.delete(rolePrivilegeToUpdate);
                                        return new RolePrivilegeResponseDto(); // Or appropriate response
                                }
                                rolePrivilegeToUpdate.setPrivilegeStatus(
                                                PrivilegeStatus.valueOf(requestDto.getPrivilegeStatus()));
                        }
                }

                RolePrivilege saved = rolePrivilegeRepository.save(rolePrivilegeToUpdate);
                return rolePrivilegeMapper.toDto(saved);
        }

        @Override
        public RolePrivilegeListResponseDto getRolePrivilegesByRoleId(Long roleId) {
                Role role = roleRepository.findById(roleId)
                                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

                Map<String, RolePrivilegeListResponseDto.RolePrivilegeDetailDto> privileges = role.getRolePrivileges()
                                .stream()
                                .collect(Collectors.toMap(
                                                rp -> rp.getRestaurantPrivilege().getPrivilege().getName(),
                                                rp -> RolePrivilegeListResponseDto.RolePrivilegeDetailDto.builder()
                                                                .rolePrivilegeId(rp.getId())
                                                                .restaurantPrivilegeId(
                                                                                rp.getRestaurantPrivilege().getId())
                                                                .privilegeStatus(
                                                                                rp.getPrivilegeStatus() != null ? rp
                                                                                                .getPrivilegeStatus()
                                                                                                .name() : null)
                                                                .build(),
                                                (existing, replacement) -> existing));

                return RolePrivilegeListResponseDto.builder()
                                .roleId(roleId)
                                .rolePrivileges(privileges)
                                .build();
        }
}
