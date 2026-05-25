package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.request.TaxRequestDto;
import com.restaurent.RMS.dtos.response.TaxResponseDto;
import com.restaurent.RMS.entities.Tax;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaxMapper {
    Tax toEntity(TaxRequestDto taxRequestDto);
    TaxResponseDto toResponseDto(Tax tax);

    Tax updateTaxFromDto(TaxRequestDto taxRequestDto, @MappingTarget Tax tax);

    default Page<TaxResponseDto> toDtoPage(Page<Tax> taxPage){
        return taxPage.map(this::toResponseDto);
    };
}
