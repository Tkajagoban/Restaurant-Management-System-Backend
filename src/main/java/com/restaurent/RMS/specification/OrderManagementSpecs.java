package com.restaurent.RMS.specification;

import com.restaurent.RMS.entities.OrderSummary;
import org.springframework.data.jpa.domain.Specification;

public class OrderManagementSpecs {
    public static Specification<OrderSummary> search(String query){
        return (root, query1, criteriaBuilder) ->{
            if (query.isEmpty() || query == null)return criteriaBuilder.conjunction();

            String likeQuery = "%"+ query.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeQuery)
            );
        };
    }
}
