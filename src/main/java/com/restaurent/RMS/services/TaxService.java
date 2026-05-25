package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.TaxRequestDto;
import com.restaurent.RMS.dtos.response.TaxResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaxService {
    TaxResponseDto addTax(TaxRequestDto taxRequestDto);
    void deleteTax(Long id);

    TaxResponseDto updateTax(Long id, TaxRequestDto taxRequestDto);

    Page<TaxResponseDto> getAllTax(Integer page, Integer size);

    List<TaxResponseDto> serach(String query);
}
