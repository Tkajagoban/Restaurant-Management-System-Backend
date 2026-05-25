package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.enums.OrderStatus;
import com.restaurent.RMS.enums.OrderTypes;
import lombok.Data;

import java.util.List;
@Data
public class OrderSummaryUpdateRequestDto {
    private Long id;
    private Long orderSummaryId;
    private OrderTypes orderType;
    private OrderStatus status;
    private Long tableId;
    private Long stewardId;
    //optional
    private Long subTotal;
    private Long grandTotal;
    private List<Long> taxIds;

    private List<OrderItemUpdateRequestDto> orderItems;
}
