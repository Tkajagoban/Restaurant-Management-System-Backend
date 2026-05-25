package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.restaurantTable;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<restaurantTable, Long>, JpaSpecificationExecutor<restaurantTable> {
    boolean existsByTableNumber(String tableNumber);
    boolean existsByTableNumberAndIdNot(@NotEmpty String tableNumber, Long id);
}
