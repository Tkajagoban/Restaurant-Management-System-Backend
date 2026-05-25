package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.Restaurant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    boolean existsBy();
    boolean existsByEmail(String email);

    default Restaurant getRestaurantById(Long restaurantId) {
        return findById(restaurantId).orElse(null);
    }

}
