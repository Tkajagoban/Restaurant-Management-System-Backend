package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.response.OrderManagementDto;

import java.util.List;

public interface OrderManagementService {
    List<OrderManagementDto> searchOrderManagement(String query);
}
