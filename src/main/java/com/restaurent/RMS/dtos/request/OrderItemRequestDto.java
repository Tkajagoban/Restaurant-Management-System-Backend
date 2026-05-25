package com.restaurent.RMS.dtos.request;

import com.restaurent.RMS.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderItemRequestDto {
    private Long foodId;
    private Long quantity;
    private Long price;
    private ItemStatus status;
}
