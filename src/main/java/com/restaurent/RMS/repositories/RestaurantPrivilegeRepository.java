package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.RestaurantPrivilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantPrivilegeRepository extends JpaRepository<RestaurantPrivilege, Long> {
    Page<RestaurantPrivilege> findAllByRestaurantId(Long restId, Pageable pageable);

    Page<RestaurantPrivilege> findAllByRestaurantIdAndActiveTrue(Long restId, Pageable pageable);

    Optional<RestaurantPrivilege> findByPrivilegeIdAndRestaurantId(Long privilegeId, Long restaurantId);

}
