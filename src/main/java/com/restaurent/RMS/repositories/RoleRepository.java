package com.restaurent.RMS.repositories;


import com.restaurent.RMS.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    Optional<Role> findByRoleNameAndRestaurantId(String roleName, Long restaurantId);

    boolean existsByRoleNameAndRestaurantId(String roleName, Long restaurantId);

    List<Role> findByRestaurantId(long restaurantId);

    boolean existsByRoleNameIgnoreCaseAndRestaurant_IdAndIdNot(
            String roleName,
            Long restaurantId,
            Long id
    );
    Role findByRoleName(String roleName);

    Optional<Role> findByRoleNameIgnoreCaseAndRestaurant_Id(
            String roleName, Long restaurantId);
}
