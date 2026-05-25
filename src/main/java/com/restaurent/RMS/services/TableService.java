package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.response.TableResponseDto;
import org.springframework.data.domain.Page;
import com.restaurent.RMS.dtos.request.TableRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TableService {
    TableResponseDto getTableById(Long id);
    Page<TableResponseDto> getAllTable(int page, int size);

    void deleteTable(Long id);
    TableRequestDto addTable(TableRequestDto tableRequestDto);

    TableResponseDto updateTable (Long id , TableRequestDto tableRequestDto);

    List<TableResponseDto> searchTable(String query);
}
