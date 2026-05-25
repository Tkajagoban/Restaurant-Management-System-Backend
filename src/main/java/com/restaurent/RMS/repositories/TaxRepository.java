package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.Tax;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long>, JpaSpecificationExecutor<Tax> {
    boolean existsByName(String name);

    boolean existsById(Long id);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByNameAndPercentage(String name, Double percentage);

    boolean existsByNameAndPercentageAndIdNot(String name, Double percentage, Long id);

    List<Tax> findByStatusTrue();
}
