package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.request.OrderItemRequestDto;
import com.restaurent.RMS.dtos.request.OrderItemUpdateRequestDto;
import com.restaurent.RMS.dtos.request.OrderSummaryUpdateRequestDto;
import com.restaurent.RMS.dtos.request.OrderSummaryRequestDto;
import com.restaurent.RMS.dtos.response.OrderSummaryResponseDto;
import com.restaurent.RMS.entities.OrderItem;
import com.restaurent.RMS.entities.OrderSummary;
import com.restaurent.RMS.entities.Tax;
import com.restaurent.RMS.entities.User;
import com.restaurent.RMS.entities.restaurantTable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

@Mapper(componentModel = "spring", uses = { OrderItemMapper.class }, imports = { DateTimeFormatter.class, ZoneId.class,
                List.class, Tax.class })
public interface OrderSummaryMapper {

        // ========= UPDATE ORDER SUMMARY =========
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "restaurantTables", source = "table")
        @Mapping(target = "steward", source = "steward")
        @Mapping(target = "status", source = "dto.status")
        @Mapping(target = "orderType", source = "dto.orderType")
        @Mapping(target = "createAt", ignore = true)
        @Mapping(target = "updateAt", ignore = true)
        @Mapping(target = "orderItems", ignore = true)
        @Mapping(target = "taxes", ignore = true)
        void updateOrderSummary(
                        @MappingTarget OrderSummary orderSummary,
                        OrderSummaryUpdateRequestDto dto,
                        restaurantTable table,
                        User steward);

        // ========= UPDATE ORDER ITEM =========
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "food", ignore = true)
        @Mapping(target = "createAt", ignore = true)
        @Mapping(target = "updateAt", ignore = true)
        void updateOrderItem(
                        @MappingTarget OrderItem orderItem,
                        OrderItemUpdateRequestDto dto);

        // ========= ENTITY → RESPONSE DTO =========
        @Mapping(target = "tableId", source = "restaurantTables.id")
        @Mapping(target = "tableNumber", source = "restaurantTables.tableNumber")
        @Mapping(target = "stewardId", source = "steward.id")
        @Mapping(target = "stewardName", expression = "java(order.getSteward() != null ? order.getSteward().getFirstName() + \" \" + order.getSteward().getLastName() : null)")
        @Mapping(target = "taxIds", source = "taxes")
        @Mapping(target = "taxTotal", expression = "java(calculateTaxTotal(order))")
        @Mapping(target = "serviceCharge", expression = "java(calculateServiceCharge(order))")
        @Mapping(target = "grandTotal", expression = "java((order.getSubTotal() != null ? order.getSubTotal() : 0L) + calculateTaxTotal(order) + calculateServiceCharge(order))")
        @Mapping(target = "restaurantTable", source = "restaurantTables.tableNumber")
        @Mapping(target = "createdDateTime", expression = "java(java.time.format.DateTimeFormatter.ofPattern(\"MM/dd/yyyy, hh:mm:ss a\")"
                        +
                        ".withZone(java.time.ZoneId.systemDefault())" +
                        ".format(order.getCreateAt()))")
        OrderSummaryResponseDto toDto(OrderSummary order);

        // ========= HELPER METHODS FOR TAX CALCULATION =========
        default List<Long> mapTaxIds(List<Tax> taxes) {
                if (taxes == null) {
                        return null;
                }
                return taxes.stream().map(Tax::getId).toList();
        }

        default Long calculateTaxTotal(OrderSummary order) {
                if (order == null || order.getTaxes() == null || order.getSubTotal() == null) {
                        return 0L;
                }
                return order.getTaxes().stream()
                                .filter(tax -> !isServiceCharge(tax.getName()))
                                .mapToLong(tax -> calculateTaxAmount(order.getSubTotal(), tax.getPercentage()))
                                .sum();
        }

        default Long calculateServiceCharge(OrderSummary order) {
                if (order == null || order.getTaxes() == null || order.getSubTotal() == null) {
                        return 0L;
                }
                return order.getTaxes().stream()
                                .filter(tax -> isServiceCharge(tax.getName()))
                                .mapToLong(tax -> calculateTaxAmount(order.getSubTotal(), tax.getPercentage()))
                                .sum();
        }

        default Long calculateGrandTotal(OrderSummary order) {
                if (order == null || order.getSubTotal() == null) {
                        return 0L;
                }
                return order.getSubTotal() + calculateTaxTotal(order) + calculateServiceCharge(order);
        }

        default boolean isServiceCharge(String taxName) {
                if (taxName == null)
                        return false;
                String name = taxName.toLowerCase().trim();
                // Match "service charge", "sc", "s.c.", "svc", "service amt"
                return name.contains("service") ||
                                name.equals("sc") ||
                                name.equals("s.c.") ||
                                name.equals("svc") ||
                                name.startsWith("sc ") ||
                                name.startsWith("s.c. ");
        }

        default Long calculateTaxAmount(Long subtotal, Double taxPercentage) {
                if (subtotal == null || taxPercentage == null) {
                        return 0L;
                }
                return Math.round((subtotal * taxPercentage) / 100.0);
        }

        // ========= REQUEST DTO → ENTITY =========
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "restaurantTables", ignore = true)
        @Mapping(target = "steward", ignore = true)
        @Mapping(target = "taxes", ignore = true)
        @Mapping(target = "orderItems", ignore = true)
        @Mapping(source = "orderstatus", target = "status")
        @Mapping(source = "subtotal", target = "subTotal")
        OrderSummary toEntity(OrderSummaryRequestDto dto);

        // ========= ENTITY → REQUEST DTO =========
        @Mapping(source = "status", target = "orderstatus")
        @Mapping(source = "subTotal", target = "subtotal")
        @Mapping(target = "resturantTablesId", source = "restaurantTables.id")
        @Mapping(target = "stewardId", source = "steward.id")
        @Mapping(target = "taxIds", source = "taxes")
        OrderSummaryRequestDto toRequestDto(OrderSummary order);

        @Mapping(target = "foodId", source = "food.id")
        OrderItemRequestDto toOrderItemRequestDto(OrderItem item);
}
