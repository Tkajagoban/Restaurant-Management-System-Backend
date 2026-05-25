package com.restaurent.RMS.dtos.response;

import com.restaurent.RMS.enums.OrderStatus;
import com.restaurent.RMS.enums.OrderTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderSummaryResponseDto {
    private Long id;
    private String orderId;
    private Long tableId;
    private String tableNumber;
    private Long stewardId;
    private String stewardName;
    private List<OrderItemResponseDto> orderItems;
    private Long subTotal;
    private Long taxTotal;
    private Long serviceCharge;
    private Long grandTotal;
    private List<Long> taxIds;
    private OrderTypes orderType;
    private OrderStatus status;
    private String createdDateTime;
    private String restaurantTable;
}
