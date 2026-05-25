package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.enums.OrderStatus;
import com.restaurent.RMS.enums.OrderTypes;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderSummaryRequestDto {

    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)

    private Long grandTotal;
    private String orderId;
    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)

    private OrderTypes orderType;
    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)

    private OrderStatus orderstatus;
    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)

    private Long subtotal;

    private Long resturantTablesId;

    private Long stewardId;
    // Price removed as per requirement

    // Item status removed from summary level

    private List<OrderItemRequestDto> orderItems;

    @NotNull(message = ValidationMessages.REQUIRED_DATA_MISSING)
    private List<Long> taxIds;

}
