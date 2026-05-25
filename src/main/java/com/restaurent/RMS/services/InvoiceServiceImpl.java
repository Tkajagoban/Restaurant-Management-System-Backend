package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.response.InvoiceResponseDto;
import com.restaurent.RMS.entities.Invoice;
import com.restaurent.RMS.mappers.InvoiceMapper;
import com.restaurent.RMS.repositories.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public Page<InvoiceResponseDto> getAllInvoices(Pageable pageable) {
        Page<Invoice> invoices = invoiceRepository.findAll(pageable);
        return invoices.map(invoiceMapper::toDto);
    }
}
