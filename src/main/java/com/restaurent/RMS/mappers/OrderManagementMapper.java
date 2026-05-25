package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.response.OrderManagementDto;
import com.restaurent.RMS.entities.OrderSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { OrderItemMapper.class })
public interface OrderManagementMapper {

    @Mapping(target = "steward", expression = "java(orderSummary.getSteward() != null ? orderSummary.getSteward().getFirstName() + \" \" + orderSummary.getSteward().getLastName() : null)")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "restaurantTables", source = "restaurantTables.tableNumber")
    @Mapping(target = "serviceCharge", ignore = true)
    @Mapping(target = "tax", ignore = true)
    OrderManagementDto toDto(OrderSummary orderSummary);
}
