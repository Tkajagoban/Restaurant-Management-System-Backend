package com.restaurent.RMS.services;

import org.springframework.stereotype.Service;
import com.restaurent.RMS.dtos.request.OrderSummaryRequestDto;
import com.restaurent.RMS.dtos.response.OrderSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.restaurent.RMS.dtos.request.OrderSummaryUpdateRequestDto;

@Service
public interface OrderSummaryService {
    void OrderSummary_deleteById(Long id);

    OrderSummaryRequestDto SaveOrderSummary(OrderSummaryRequestDto orderSummaryRequestDto);

    OrderSummaryUpdateRequestDto updateOrder(OrderSummaryUpdateRequestDto requestDto, Long id);

    Page<OrderSummaryResponseDto> getAllOrders(Pageable pageable);

    OrderSummaryResponseDto getOrderById(Long id);
}
