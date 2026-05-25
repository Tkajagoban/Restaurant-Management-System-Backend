package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.TaxRequestDto;
import com.restaurent.RMS.dtos.response.TaxResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.TaxService;
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
public class TaxController {

    private final TaxService taxService;

    @PostMapping(EndpointBundle.TAX_ADDED)
    public ResponseEntity<ResponseWrapper<TaxResponseDto>> addTax (@Valid @RequestBody TaxRequestDto taxRequestDto){
        TaxResponseDto taxResponseDto = taxService.addTax(taxRequestDto);

        ResponseWrapper<TaxResponseDto> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.CREATED.getCode(),
                RestApiResponseStatusCodes.CREATED.getMessage(),
                taxResponseDto
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
    }

    @PutMapping(EndpointBundle.TAX_ID)
    public ResponseEntity<ResponseWrapper<TaxResponseDto>> updateTax(@PathVariable Long id, @Valid @RequestBody TaxRequestDto taxRequestDto){
        TaxResponseDto taxResponseDto = taxService.updateTax(id,taxRequestDto);

        ResponseWrapper<TaxResponseDto> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.UPDATED.getCode(),
                RestApiResponseStatusCodes.UPDATED.getMessage(),
                taxResponseDto
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
    }

    @GetMapping(EndpointBundle.TAX)
    public ResponseEntity<ResponseWrapper<Page<TaxResponseDto>>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ){
        Page<TaxResponseDto> taxResponseDtoList = taxService.getAllTax(page, size);
        ResponseWrapper<Page<TaxResponseDto>> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                taxResponseDtoList
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
    }

    @DeleteMapping(EndpointBundle.TAX_ID)
    public ResponseEntity<ResponseWrapper<Void>> deleteTax (@PathVariable Long id){
        taxService.deleteTax(id);
        ResponseWrapper<Void> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.DELETED.getCode(),
                ValidationMessages.DELETE_SUCCESS,
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping(EndpointBundle.TAX_SEARCH)
    public ResponseEntity<ResponseWrapper<List<TaxResponseDto>>> serachTaxs(@RequestParam(required = false) String query){
        List<TaxResponseDto> responseDtos =  taxService.serach(query);
        if (responseDtos.isEmpty()){
            ResponseWrapper<List<TaxResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    RestApiResponseStatusCodes.NOT_FOUND.getMessage(),
                    null
            );
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        } else {
            ResponseWrapper<List<TaxResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                    responseDtos
            );
            return  ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }

    }
}
