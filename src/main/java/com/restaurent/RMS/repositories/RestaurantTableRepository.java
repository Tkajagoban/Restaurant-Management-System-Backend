package com.restaurent.RMS.repositories;

import com.restaurent.RMS.entities.restaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<restaurantTable, Long> {
}
