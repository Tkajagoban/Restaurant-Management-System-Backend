package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.response.InvoiceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface InvoiceService {
    Page<InvoiceResponseDto> getAllInvoices(Pageable pageable);
}
