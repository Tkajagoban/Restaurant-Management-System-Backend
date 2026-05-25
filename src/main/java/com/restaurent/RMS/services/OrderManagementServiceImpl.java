package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.response.OrderManagementDto;
import com.restaurent.RMS.entities.OrderSummary;
import com.restaurent.RMS.mappers.OrderManagementMapper;
import com.restaurent.RMS.repositories.OrderManagementRepositiory;
import com.restaurent.RMS.specification.OrderManagementSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderManagementServiceImpl implements OrderManagementService {

    private final OrderManagementRepositiory orderManagementRepositiory;
    private final OrderManagementMapper orderManagementMapper;

    @Override
    public List<OrderManagementDto> searchOrderManagement(String query) {
        List<OrderSummary> orderManagementList = orderManagementRepositiory.findAll(OrderManagementSpecs.search(query));
        return orderManagementList.stream()
                .map(orderManagementMapper::toDto)
                .toList();
    }
}
