package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.MainCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MainCategoriesRepository extends JpaRepository<MainCategories, Long> {

    boolean existsByName(String name);

    boolean existsByRestaurant_IdAndNameIgnoreCaseAndIdNot(Long restaurantId, String categoryName, Long id);
    boolean existsByNameAndRestaurantId(String name, Long restaurantId);
}
