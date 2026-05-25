package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.RolePrivilege;
import com.restaurent.RMS.enums.PrivilegeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {
    Optional<RolePrivilege> findByRole_IdAndRestaurantPrivilege_Id(Long roleId, Long restaurantPrivilegeId);

    Optional<RolePrivilege> findByRole_RoleNameAndRestaurantPrivilege_Privilege_Name(String roleName,
            String privilegeName);

    @Query("SELECT COUNT(rp) FROM RolePrivilege rp " +
            "JOIN rp.restaurantPrivilege resp " +
            "JOIN resp.privilege p " +
            "WHERE rp.role.id = :roleId " +
            "AND (LOWER(p.name) = 'role privileges' OR LOWER(p.name) = 'roleprivileges' OR LOWER(p.name) = 'role management') "
            +
            "AND rp.privilegeStatus = :status")
    Long countAdministrativePrivileges(@Param("roleId") Long roleId, @Param("status") PrivilegeStatus status);
}
