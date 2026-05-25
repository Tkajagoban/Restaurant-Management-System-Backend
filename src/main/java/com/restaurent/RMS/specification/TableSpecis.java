package com.restaurent.RMS.specification;

import com.restaurent.RMS.entities.restaurantTable;
import org.springframework.data.jpa.domain.Specification;

public class TableSpecis {
    public static Specification<restaurantTable> search(String query){
        return (root, query1, criteriaBuilder) -> {
            if (query == null || query.isEmpty()) criteriaBuilder.conjunction();

            String likeQuery = "%" + query.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("tableNumber")), likeQuery)
            );
        };
    }
}
