package com.restaurent.RMS.repositories;


import com.restaurent.RMS.entities.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderManagementRepositiory extends JpaRepository<OrderSummary,Long>, JpaSpecificationExecutor<OrderSummary> {
}
