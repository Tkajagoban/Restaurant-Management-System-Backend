package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.Food;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food ,Long>, JpaSpecificationExecutor<Food> {
    Page<Food> findByMainCategories_Id(Long mainCategoryId, Pageable pageable);
    Page<Food> findBySubCategories_Id(Long subcategoryId, Pageable pageable);
    Page<Food> findByMainCategories_IdAndSubCategories_Id(Long mainCategoryId, Long subcategoryId, Pageable pageable);
    List<Food> findBySubCategories_Id(Long subCategoryId);
    boolean existsByNameAndMainCategories_IdAndSubCategories_Id(String name, Long mainCategoryId, Long subCategoryId);
    boolean existsByNameAndMainCategories_IdAndSubCategories_IdAndIdNot(String name, Long mainCategoryId, Long subCategoryId, Long id);
}
