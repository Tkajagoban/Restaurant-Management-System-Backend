package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.OrderItemUpdateRequestDto;
import com.restaurent.RMS.dtos.request.OrderSummaryRequestDto;
import com.restaurent.RMS.dtos.request.OrderSummaryUpdateRequestDto;
import com.restaurent.RMS.dtos.response.OrderSummaryResponseDto;
import com.restaurent.RMS.entities.Food;
import com.restaurent.RMS.entities.OrderItem;
import com.restaurent.RMS.entities.OrderSummary;
import com.restaurent.RMS.entities.Tax;
import com.restaurent.RMS.entities.User;
import com.restaurent.RMS.entities.restaurantTable;
import com.restaurent.RMS.enums.OrderTypes;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.OrderSummaryMapper;
import com.restaurent.RMS.repositories.FoodRepository;
import com.restaurent.RMS.repositories.OrderItemRepository;
import com.restaurent.RMS.repositories.OrderSummaryRepository;
import com.restaurent.RMS.repositories.RestaurantTableRepository;
import com.restaurent.RMS.repositories.TableRepository;
import com.restaurent.RMS.repositories.TaxRepository;
import com.restaurent.RMS.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderSummaryServiceImpl implements OrderSummaryService {
    private final OrderSummaryRepository orderSummaryRepository;
    private final OrderSummaryMapper orderSummaryMapper;

    private final UserRepository userRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final FoodRepository foodRepository;
    private final TaxRepository taxRepository;
    private final TableRepository tableRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public void OrderSummary_deleteById(Long id) {
        OrderSummary orderSummary = orderSummaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderSummary not found with id: " + id));

        orderSummaryRepository.delete(orderSummary);
    }

    @Override
    public OrderSummaryRequestDto SaveOrderSummary(OrderSummaryRequestDto dto) {

        OrderSummary orderSummary = orderSummaryMapper.toEntity(dto);

        // ✅ Set taxes
        if (dto.getTaxIds() != null && !dto.getTaxIds().isEmpty()) {
            List<Tax> taxes = taxRepository.findAllById(dto.getTaxIds());
            orderSummary.setTaxes(taxes);
        }

        orderSummary.setSubTotal(dto.getSubtotal());
        orderSummary.setStatus(dto.getOrderstatus());
        orderSummary.setOrderType(dto.getOrderType());

        // ✅ CONDITIONAL LOGIC
        if (dto.getOrderType() == OrderTypes.DINE_IN) {

            if (dto.getStewardId() == null || dto.getResturantTablesId() == null) {
                throw new IllegalArgumentException("Steward and Table are required for DINE_IN orders");
            }

            User steward = userRepository.findById(dto.getStewardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Steward not found"));
            orderSummary.setSteward(steward);

            restaurantTable table = restaurantTableRepository.findById(dto.getResturantTablesId())
                    .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
            orderSummary.setRestaurantTables(table);

        } else {
            // ✅ TAKE_AWAY
            orderSummary.setSteward(null);
            orderSummary.setRestaurantTables(null);
        }

        // Calculate grand total before saving
        long totalTax = orderSummary.getTaxes() != null ? orderSummary.getTaxes().stream()
                .filter(t -> !orderSummaryMapper.isServiceCharge(t.getName()))
                .mapToLong(t -> Math.round(orderSummary.getSubTotal() * t.getPercentage() / 100.0))
                .sum() : 0L;
        long serviceCharge = orderSummary.getTaxes() != null ? orderSummary.getTaxes().stream()
                .filter(t -> orderSummaryMapper.isServiceCharge(t.getName()))
                .mapToLong(t -> Math.round(orderSummary.getSubTotal() * t.getPercentage() / 100.0))
                .sum() : 0L;
        orderSummary.setGrandTotal(orderSummary.getSubTotal() + totalTax + serviceCharge);

        OrderSummary savedOrder = orderSummaryRepository.save(orderSummary);

        // ✅ 2. Provide Order ID and Save Again
        savedOrder.setOrderId("ORD-" + savedOrder.getId());
        savedOrder = orderSummaryRepository.save(savedOrder);

        // ✅ Order Items
        List<com.restaurent.RMS.dtos.request.OrderItemRequestDto> savedItemDtos = new java.util.ArrayList<>();
        if (dto.getOrderItems() != null) {
            for (com.restaurent.RMS.dtos.request.OrderItemRequestDto itemDto : dto.getOrderItems()) {
                Food food = foodRepository.findById(itemDto.getFoodId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Food not found with id: " + itemDto.getFoodId()));

                OrderItem item = new OrderItem();
                item.setOrderSummary(savedOrder);
                item.setFood(food);
                item.setItemName(food.getName());
                // Use price from Food entity for security/accuracy
                item.setPrice(food.getPrice().longValue());
                item.setQuantity(itemDto.getQuantity());

                // Set status: Use specific status if provided, else default to PENDING
                if (itemDto.getStatus() != null) {
                    item.setStatus(itemDto.getStatus());
                } else {
                    item.setStatus(com.restaurent.RMS.enums.ItemStatus.PENDING); // Default
                }

                OrderItem savedItemEntity = orderItemRepository.save(item);

                // Prepare response DTO for this item
                com.restaurent.RMS.dtos.request.OrderItemRequestDto responseItemDto = new com.restaurent.RMS.dtos.request.OrderItemRequestDto();
                responseItemDto.setFoodId(food.getId());
                responseItemDto.setQuantity(savedItemEntity.getQuantity());
                responseItemDto.setPrice(savedItemEntity.getPrice());
                responseItemDto.setStatus(savedItemEntity.getStatus());
                savedItemDtos.add(responseItemDto);
            }
        }

        // ✅ Response
        OrderSummaryRequestDto response = orderSummaryMapper.toRequestDto(savedOrder);
        // Map created items back to response if needed, or just return what we have.
        // The original method was returning the input DTO fields mostly.
        // We will set the list of items for consistency.
        response.setOrderItems(savedItemDtos);
        response.setOrderId(savedOrder.getOrderId());
        // Price field removed from DTO
        response.setTaxIds(dto.getTaxIds());
        // itemstatus removed from DTO

        return response;
    }

    @Override
    public OrderSummaryResponseDto getOrderById(Long id) {
        return orderSummaryRepository.findById(id)
                .map(orderSummaryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    public Page<OrderSummaryResponseDto> getAllOrders(Pageable pageable) {
        return orderSummaryRepository.findAll(pageable)
                .map(orderSummaryMapper::toDto);

    }

    public OrderSummaryUpdateRequestDto updateOrder(OrderSummaryUpdateRequestDto requestDto, Long id) {

        // Get existing order
        OrderSummary orderSummary = orderSummaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found Order Summary ID " + id));

        restaurantTable table = null;
        User steward = null;

        // Handle order type
        if (requestDto.getOrderType() == OrderTypes.DINE_IN) {
            if (requestDto.getTableId() == null) {
                throw new ResourceNotFoundException("Table is required for DINE_IN order");
            }
            if (requestDto.getStewardId() == null) {
                throw new ResourceNotFoundException("Steward is required for DINE_IN order");
            }

            table = tableRepository.findById(requestDto.getTableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
            steward = userRepository.findById(requestDto.getStewardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Steward not found"));
        }

        // Update order summary entity
        orderSummaryMapper.updateOrderSummary(orderSummary, requestDto, table, steward);

        // Remove deleted items
        List<Long> dtoItemIds = requestDto.getOrderItems().stream()
                .map(OrderItemUpdateRequestDto::getOrderItemId)
                .toList();

        orderSummary.getOrderItems()
                .removeIf(item -> item.getId() != null && !dtoItemIds.contains(item.getId()));

        // Update / Add items
        for (OrderItemUpdateRequestDto itemDto : requestDto.getOrderItems()) {
            if (itemDto.getOrderItemId() != null) {
                OrderItem orderItem = orderItemRepository.findById(itemDto.getOrderItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));
                orderSummaryMapper.updateOrderItem(orderItem, itemDto);
            } else if (itemDto.getFoodId() != null) {
                Food food = foodRepository.findById(itemDto.getFoodId())
                        .orElseThrow(() -> new ResourceNotFoundException("Food not found"));
                OrderItem newItem = new OrderItem();
                newItem.setFood(food);
                newItem.setItemName(food.getName());
                newItem.setPrice(food.getPrice().longValue());
                newItem.setQuantity(itemDto.getQuantity());
                newItem.setStatus(itemDto.getStatus());
                newItem.setOrderSummary(orderSummary);
                orderSummary.getOrderItems().add(newItem);
            }
        }

        // Recalculate subtotal only if not passed from frontend
        if (requestDto.getSubTotal() != null) {
            orderSummary.setSubTotal(requestDto.getSubTotal());
        } else {
            long subTotal = orderSummary.getOrderItems().stream()
                    .mapToLong(i -> i.getPrice() * i.getQuantity())
                    .sum();
            orderSummary.setSubTotal(subTotal);
        }

        // Set taxes only if taxIds passed, else use active taxes
        if (requestDto.getTaxIds() != null && !requestDto.getTaxIds().isEmpty()) {
            List<Tax> taxes = taxRepository.findAllById(requestDto.getTaxIds());
            orderSummary.setTaxes(taxes);
        } else {
            List<Tax> activeTaxes = taxRepository.findByStatusTrue();
            orderSummary.setTaxes(activeTaxes);
        }

        // Recalculate grand total if not passed
        // Recalculate grand total using consistent mapper-like logic for persistence
        long totalTaxVal = orderSummary.getTaxes() != null ? orderSummary.getTaxes().stream()
                .filter(t -> !orderSummaryMapper.isServiceCharge(t.getName()))
                .mapToLong(t -> Math.round(orderSummary.getSubTotal() * t.getPercentage() / 100.0))
                .sum() : 0L;
        long scVal = orderSummary.getTaxes() != null ? orderSummary.getTaxes().stream()
                .filter(t -> orderSummaryMapper.isServiceCharge(t.getName()))
                .mapToLong(t -> Math.round(orderSummary.getSubTotal() * t.getPercentage() / 100.0))
                .sum() : 0L;
        orderSummary.setGrandTotal(orderSummary.getSubTotal() + totalTaxVal + scVal);

        // Save
        orderSummaryRepository.save(orderSummary);

        // Build response
        OrderSummaryUpdateRequestDto response = new OrderSummaryUpdateRequestDto();
        response.setId(orderSummary.getId());
        response.setOrderSummaryId(orderSummary.getId());
        response.setOrderType(orderSummary.getOrderType());
        response.setStatus(orderSummary.getStatus());
        response.setSubTotal(orderSummary.getSubTotal());
        response.setGrandTotal(orderSummary.getGrandTotal());

        if (orderSummary.getRestaurantTables() != null) {
            response.setTableId(orderSummary.getRestaurantTables().getId());
        }
        if (orderSummary.getSteward() != null) {
            response.setStewardId(orderSummary.getSteward().getId());
        }

        if (orderSummary.getTaxes() != null) {
            response.setTaxIds(orderSummary.getTaxes().stream()
                    .map(Tax::getId)
                    .collect(Collectors.toList()));
        }

        List<OrderItemUpdateRequestDto> responseItems = orderSummary.getOrderItems().stream()
                .map(item -> {
                    OrderItemUpdateRequestDto dto = new OrderItemUpdateRequestDto();
                    dto.setOrderItemId(item.getId());
                    if (item.getFood() != null)
                        dto.setFoodId(item.getFood().getId());
                    dto.setQuantity(item.getQuantity());
                    dto.setStatus(item.getStatus());
                    return dto;
                }).collect(Collectors.toList());
        response.setOrderItems(responseItems);

        return response;
    }
}
