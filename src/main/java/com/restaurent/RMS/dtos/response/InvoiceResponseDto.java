package com.restaurent.RMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceResponseDto {
    private Long id;
    private String invoiceId;
    private String dateTime;
    private OrderSummaryDto orderSummary;
}
