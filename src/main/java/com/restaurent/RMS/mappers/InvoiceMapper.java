package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.response.InvoiceResponseDto;
import com.restaurent.RMS.dtos.response.OrderItemDto;
import com.restaurent.RMS.dtos.response.OrderSummaryDto;
import com.restaurent.RMS.entities.Invoice;
import com.restaurent.RMS.entities.OrderItem;
import com.restaurent.RMS.entities.OrderSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    // ========= INVOICE ENTITY → INVOICE RESPONSE DTO =========
    @Mapping(target = "dateTime", expression = "java(java.time.format.DateTimeFormatter.ofPattern(\"MM/dd/yyyy, hh:mm:ss a\")"
            + ".withZone(java.time.ZoneId.systemDefault())"
            + ".format(invoice.getDateTime()))")
    @Mapping(target = "orderSummary", source = "orderSummary")
    InvoiceResponseDto toDto(Invoice invoice);

    // ========= ORDER SUMMARY ENTITY → ORDER SUMMARY DTO =========
    @Mapping(target = "tableId", source = "restaurantTables.id")
    @Mapping(target = "stewardId", source = "steward.id")
    @Mapping(target = "subtotal", source = "subTotal")
    @Mapping(target = "tax", expression = "java(orderSummary.getTaxes() != null ? "
            + "orderSummary.getTaxes().stream()"
            + ".mapToLong(tax -> calculateTaxAmount(orderSummary.getSubTotal(), tax.getPercentage()))"
            + ".sum() : 0L)")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderSummaryDto toOrderSummaryDto(OrderSummary orderSummary);

    // ========= ORDER ITEM ENTITY → ORDER ITEM DTO =========
    @Mapping(target = "foodName", source = "food.name")
    OrderItemDto toOrderItemDto(OrderItem orderItem);

    // ========= HELPER METHOD FOR TAX CALCULATION =========
    default Long calculateTaxAmount(Long subtotal, Double taxPercentage) {
        if (subtotal == null || taxPercentage == null) {
            return 0L;
        }
        return Math.round((subtotal * taxPercentage) / 100);
    }

    // ========= LIST MAPPING =========
    List<OrderItemDto> toOrderItemDtoList(List<OrderItem> orderItems);
}
