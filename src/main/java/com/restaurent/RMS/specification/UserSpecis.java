package com.restaurent.RMS.specification;

import com.restaurent.RMS.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecis {
    public static Specification<User> search(String query){
        return (root, query1, criteriaBuilder) -> {
            if (query == null || query.isEmpty()) return criteriaBuilder.conjunction();

            String likeQuery = "%" + query.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likeQuery),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likeQuery)

            );
        };
    }
}
