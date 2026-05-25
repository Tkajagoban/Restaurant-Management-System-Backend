package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.TableRequestDto;
import com.restaurent.RMS.dtos.response.TableResponseDto;
import com.restaurent.RMS.dtos.response.TaxResponseDto;
import com.restaurent.RMS.dtos.response.UserResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.TableService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.SETTINGS)
@RequiredArgsConstructor
public class TableController {
    public final TableService tableService;

    @GetMapping(EndpointBundle.TABLE)
    public ResponseEntity<ResponseWrapper<Page<TableResponseDto>>> getAlltable(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {

        int pageNumber = (page == null) ? 0 : page;
        int pageSize   = (size == null) ? 10 : size;

        Page<TableResponseDto> Tables = tableService.getAllTable(pageNumber, pageSize);
     ResponseWrapper<Page<TableResponseDto>> response = new ResponseWrapper<>(
             RestApiResponseStatusCodes.SUCCESS.getCode(),
             ValidationMessages.RETRIEVED,
             Tables
     );
     return ResponseEntity.ok(response);
    }

    @GetMapping(EndpointBundle.TABLE_ID)
    public ResponseEntity<ResponseWrapper<TableResponseDto>> getTableById(@PathVariable Long id) {
        TableResponseDto getTable=tableService.getTableById(id);

        ResponseWrapper<TableResponseDto> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.RETRIEVED,
                getTable
        );
        return ResponseEntity.ok(response);
    }



    @DeleteMapping(EndpointBundle.TABLE_ID)
    public ResponseEntity<ResponseWrapper<Object>> deleteById (@PathVariable Long id){
        tableService.deleteTable(id);
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.DELETED.getCode(),
                        ValidationMessages.DELETE_SUCCESS,
                        null
                )
        );
    }

    @PostMapping(EndpointBundle.TABLE_ADDED)
    public ResponseEntity<ResponseWrapper<TableRequestDto>> createTable
            (@Valid @RequestBody TableRequestDto tableRequestDto){
        TableRequestDto createTable = tableService.addTable(tableRequestDto);
        ResponseWrapper<TableRequestDto> response=
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        RestApiResponseStatusCodes.SUCCESS.getMessage(),
                        createTable
                );
        return ResponseEntity.ok(response);
    }

    @PutMapping(EndpointBundle.TABLE_ID)
    public ResponseEntity<ResponseWrapper<TableResponseDto>> updateTable(
            @PathVariable Long id,
            @Valid @RequestBody TableRequestDto tableRequestDto) {

        TableResponseDto updatedTable =
                tableService.updateTable(id, tableRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.SUCCESS.getCode(),
                        ValidationMessages.UPDATED,
                        updatedTable
                )
        );
    }

    @GetMapping(EndpointBundle.TABLE_SEARCH)
    public ResponseEntity<ResponseWrapper<List<TableResponseDto>>> searchTables(@RequestParam(required = false) String query){
        List<TableResponseDto> responseDtos = tableService.searchTable(query);
        if (responseDtos.isEmpty()){
            ResponseWrapper<List<TableResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    RestApiResponseStatusCodes.NOT_FOUND.getMessage(),
                    null
            );
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        } else {
            ResponseWrapper<List<TableResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                    responseDtos
            );
            return  ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }
    }
}
