package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.TableRequestDto;
import com.restaurent.RMS.dtos.response.TableResponseDto;
import com.restaurent.RMS.entities.restaurantTable;
import com.restaurent.RMS.exceptionHandlers.AlreadyExistException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.TableMapper;
import com.restaurent.RMS.repositories.TableRepository;
import com.restaurent.RMS.specification.TableSpecis;
import com.restaurent.RMS.utils.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
        private final TableRepository tableRepository;
        private final TableMapper tableMapper;


        @Override
        public Page<TableResponseDto> getAllTable(int page, int size) {

            if (page < 0 || size <= 0) {
                throw new IllegalArgumentException(
                        page < 0 && size <= 0
                                ? "Page index and page size must not be negative"
                                : page < 0
                                ? "Page index must not be negative"
                                : "Page size must be greater than zero"
                );
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<restaurantTable> tables = tableRepository.findAll(pageable);

            if (tables.isEmpty()) {
                throw new ResourceNotFoundException("Tables not found");
            }

            return tables.map(tableMapper::toDto);
        }

    @Override
    public TableResponseDto getTableById(Long id) {
        restaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant Table " + RestApiResponseStatusCodes.NOT_FOUND.getMessage()
                ));

        return tableMapper.toResponseDto(table);
    }

    @Override
    public void deleteTable(Long id) {
        restaurantTable table= tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        tableRepository.delete(table);
    }

    @Override
    public TableRequestDto addTable(TableRequestDto tableRequestDto){
        if (tableRepository.existsByTableNumber(tableRequestDto.getTableNumber())) {
            throw new AlreadyExistException(
                    ValidationMessages.ALREADY_EXISTS
            );
        }


        restaurantTable table = tableMapper.toEntity(tableRequestDto);
        restaurantTable savedTable = tableRepository.save(table);

        return tableMapper.toDtos(savedTable);
    }

    @Override
    public TableResponseDto updateTable (Long id , TableRequestDto tableRequestDto) {
        restaurantTable existingTable = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));

        if (tableRepository.existsByTableNumberAndIdNot(tableRequestDto.getTableNumber(), id)) {
            throw new AlreadyExistException( "Table already exists :" +id
            );


        }
        existingTable.setTableNumber(tableRequestDto.getTableNumber());
        existingTable.setGuestCount(tableRequestDto.getGuestCount());
        existingTable.setStatus(tableRequestDto.getStatus());


        restaurantTable updatedTable = tableRepository.save(existingTable);
        return tableMapper.toDto(updatedTable);
    }

    @Override
    public List<TableResponseDto> searchTable(String query) {
            List<restaurantTable> restaurantTables = tableRepository.findAll(TableSpecis.search(query));

        return restaurantTables.stream()
                .map(tableMapper::toResponseDto)
                .toList();
    }
}
