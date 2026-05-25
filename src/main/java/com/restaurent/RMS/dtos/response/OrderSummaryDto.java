package com.restaurent.RMS.dtos.response;

import com.restaurent.RMS.enums.OrderTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderSummaryDto {
    private String orderId;
    private OrderTypes orderType;
    private Long tableId;
    private Long stewardId;
    private Long subtotal;
    private Long grandTotal;
    private Long tax;
    private List<OrderItemDto> orderItems;
}
