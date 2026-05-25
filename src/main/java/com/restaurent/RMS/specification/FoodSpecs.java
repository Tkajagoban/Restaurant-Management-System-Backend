package com.restaurent.RMS.specification;

import com.restaurent.RMS.entities.Food;
import org.springframework.data.jpa.domain.Specification;

public class FoodSpecs {
    public static Specification<Food> search(String query){
        return (root, query1, criteriaBuilder) -> {
            if (query == null || query.isEmpty()) return criteriaBuilder.conjunction();

            String likeQuery = "%"+ query.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeQuery)
            );
        };
    }
}
