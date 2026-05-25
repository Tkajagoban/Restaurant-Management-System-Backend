package com.restaurent.RMS.specification;

import com.restaurent.RMS.entities.Role;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecs {
    public static Specification<Role> search(String query){
        return (root, query1, criteriaBuilder) -> {
            if(query == null || query.isEmpty()) return criteriaBuilder.conjunction();

            String likeQuery = "%" + query.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("roleName").as(String.class)), likeQuery)
            );
        };
    }
}
