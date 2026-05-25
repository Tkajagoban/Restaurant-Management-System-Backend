package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.enums.ItemStatus;
import lombok.Data;

@Data
public class OrderItemUpdateRequestDto {
    private Long orderItemId;
    private Long foodId;
    private Long quantity;
    private ItemStatus status;
}
