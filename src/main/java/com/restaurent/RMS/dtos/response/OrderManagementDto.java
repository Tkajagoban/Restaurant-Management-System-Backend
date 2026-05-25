package com.restaurent.RMS.dtos.response;

import com.restaurent.RMS.entities.OrderItem;
import com.restaurent.RMS.entities.User;
import com.restaurent.RMS.entities.restaurantTable;
import com.restaurent.RMS.enums.OrderStatus;
import com.restaurent.RMS.enums.OrderTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderManagementDto {
    private Long id;
    private String steward;
    private List<OrderItemResponseDto> orderItems;
    private Long subTotal;
    private Long serviceCharge;
    private Long tax;
    private Enum orderType;
    private Enum status;
    private String restaurantTables;
}
