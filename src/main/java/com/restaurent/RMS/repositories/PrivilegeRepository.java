package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege , Long> {
}
