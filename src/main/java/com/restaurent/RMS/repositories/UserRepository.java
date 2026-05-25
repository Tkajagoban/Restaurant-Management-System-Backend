package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.User;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    boolean existsByEmail(String email);

    boolean existsByNic(String nic);

    boolean existsByPhoneNumber(@NotEmpty String phoneNumber);

    @Query("""
                SELECT u
                FROM User u
                JOIN FETCH u.role r
                LEFT JOIN FETCH r.restaurant
                WHERE u.id = :id
            """)
    Optional<User> findByIdWithRoleAndRestaurant(Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByNicAndIdNot(String nic, Long id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    Page<User> findByRole_Restaurant_Id(Long restaurantId, Pageable pageable);

    // Get users filtered by role (same restaurant)
    Page<User> findByRole_Restaurant_IdAndRole_Id(
            Long restaurantId,
            Long roleId,
            Pageable pageable);

    @Query("""
                SELECT u
                FROM User u
                JOIN FETCH u.role r
                LEFT JOIN FETCH r.rolePrivileges rp
                LEFT JOIN FETCH rp.restaurantPrivilege resp
                LEFT JOIN FETCH resp.privilege p
                WHERE u.email = :email
            """)
    Optional<User> findByEmailWithRoleAndPrivileges(String email);

    Optional<User> findByEmail(String email);

    List<User> findByRole_IdAndRole_Restaurant_Id(long id, Long restaurantId);
}
