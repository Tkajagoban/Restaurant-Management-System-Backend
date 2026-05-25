package com.restaurent.RMS.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PLACE_ORDER,
    REJECTED,
    IN_PREPARED,
    READY_TO_SERVE,
    CANCELLED,
    READY_TO_ORDER
}
