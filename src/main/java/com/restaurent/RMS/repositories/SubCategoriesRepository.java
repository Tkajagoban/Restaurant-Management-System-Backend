package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.SubCategories;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface SubCategoriesRepository extends JpaRepository<SubCategories, Long> {
    boolean existsByNameAndMainCategories_Id(String name, Long mainCategoryId);
    boolean existsByNameAndMainCategories_IdAndIdNot(String name, Long mainCategoryId, Long id);
    List<SubCategories> findByMainCategories_Id(Long mainCategoryId);

}
