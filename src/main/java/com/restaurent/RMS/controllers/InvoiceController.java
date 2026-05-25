package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.response.InvoiceResponseDto;
import com.restaurent.RMS.services.InvoiceService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndpointBundle.SETTINGS)
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping(EndpointBundle.INVOICES)
    public ResponseEntity<PageResponse<InvoiceResponseDto>> getAllInvoices(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sort) {

        // Create pageable with sorting
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        // Get paginated invoices from service
        Page<InvoiceResponseDto> invoicePage = invoiceService.getAllInvoices(pageable);

        // Wrap in PageResponse
        PageResponse<InvoiceResponseDto> response = new PageResponse<>(
                invoicePage.getContent(),
                invoicePage.getNumber(),
                invoicePage.getSize(),
                invoicePage.getTotalElements(),
                invoicePage.getTotalPages(),
                invoicePage.isFirst(),
                invoicePage.isLast());

        return ResponseEntity.ok(response);
    }
}
