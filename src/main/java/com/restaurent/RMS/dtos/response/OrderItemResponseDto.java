package com.restaurent.RMS.dtos.response;

import com.restaurent.RMS.enums.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemResponseDto {
    private Long id;
    private String itemName;
    private String foodName;
    private ItemStatus status;

    private Long quantity;
    private Long price;
}
