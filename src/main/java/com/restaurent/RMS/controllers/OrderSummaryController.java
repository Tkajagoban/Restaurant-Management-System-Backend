package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.OrderSummaryRequestDto;
import com.restaurent.RMS.dtos.request.OrderSummaryUpdateRequestDto;
import com.restaurent.RMS.dtos.response.OrderSummaryResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.OrderSummaryService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointBundle.SETTINGS)
public class OrderSummaryController {
        private final OrderSummaryService orderSummaryService;

        @DeleteMapping(EndpointBundle.ORDER_SUMMARY_BY_ID)
        public ResponseEntity<ResponseWrapper<Object>> deleteById(@PathVariable Long id) {
                orderSummaryService.OrderSummary_deleteById(id);
                return ResponseEntity.ok(
                                new ResponseWrapper<>(
                                                RestApiResponseStatusCodes.DELETED.getCode(),
                                                ValidationMessages.DELETE_SUCCESS,
                                                null));
        }

        @PostMapping(EndpointBundle.ORDER_SUMMARY_ADD)
        public ResponseEntity<ResponseWrapper<OrderSummaryRequestDto>> toSave(
                        @RequestBody OrderSummaryRequestDto orderSummaryRequestDto) {
                OrderSummaryRequestDto ors = orderSummaryService.SaveOrderSummary(orderSummaryRequestDto);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                                RestApiResponseStatusCodes.SUCCESS.getCode(),
                                ValidationMessages.SAVED_SUCCESSFULL,
                                ors));

        }

        @GetMapping(EndpointBundle.ORDERS)
        public ResponseEntity<List<OrderSummaryResponseDto>> getAllOrders(
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "100") Integer size) {

                Pageable pageable = PageRequest.of(page, size);

                Page<OrderSummaryResponseDto> orders = orderSummaryService.getAllOrders(pageable);

                return ResponseEntity.ok(orders.getContent());
        }

        @PutMapping(EndpointBundle.ORDER_SUMMARY_BY_ID)
        public ResponseEntity<ResponseWrapper<OrderSummaryUpdateRequestDto>> updateOrder(
                        @RequestBody OrderSummaryUpdateRequestDto requestDto, @PathVariable Long id) {
                OrderSummaryUpdateRequestDto ors = orderSummaryService.updateOrder(requestDto, id);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                                RestApiResponseStatusCodes.SUCCESS.getCode(),
                                ValidationMessages.UPDATED,
                                ors));

        }

        @GetMapping(EndpointBundle.ORDER_SUMMARY_BY_ID)
        public ResponseEntity<ResponseWrapper<OrderSummaryResponseDto>> getOrderById(@PathVariable Long id) {
                OrderSummaryResponseDto order = orderSummaryService.getOrderById(id);
                return ResponseEntity.ok(new ResponseWrapper<>(
                                RestApiResponseStatusCodes.SUCCESS.getCode(),
                                ValidationMessages.RETRIEVED,
                                order));
        }
}
