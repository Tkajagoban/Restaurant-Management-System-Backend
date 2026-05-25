package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.response.OrderItemResponseDto;
import com.restaurent.RMS.entities.OrderItem;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderSummary", ignore = true)
    @Mapping(target = "itemName", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "food", ignore = true)
    OrderItem toEntity(com.restaurent.RMS.dtos.request.OrderItemRequestDto dto);

    @Mapping(target = "foodName", source = "food.name")
    @Mapping(target = "itemName", source = "food.name")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
