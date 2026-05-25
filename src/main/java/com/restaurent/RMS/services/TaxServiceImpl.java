package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.TaxRequestDto;
import com.restaurent.RMS.dtos.response.TaxResponseDto;
import com.restaurent.RMS.entities.Tax;
import com.restaurent.RMS.exceptionHandlers.AlreadyExistException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.TaxMapper;
import com.restaurent.RMS.repositories.TaxRepository;
import com.restaurent.RMS.specification.TaxSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;
    private final TaxMapper taxMapper;

    @Override
    public TaxResponseDto addTax(TaxRequestDto taxRequestDto) {
        validateDuplicate(taxRequestDto.getName(), taxRequestDto.getPercentage(), null);

        Tax tax = taxMapper.toEntity(taxRequestDto);
        Tax savedTax = taxRepository.save(tax);

        return taxMapper.toResponseDto(savedTax);
    }

    @Override
    public void deleteTax(Long id) {
        taxRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tax not found"));
        taxRepository.deleteById(id);

    }

    @Override
    public TaxResponseDto updateTax(Long id, TaxRequestDto taxRequestDto) {
        Tax existingTax = taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("tax id not found"));

        validateDuplicate(taxRequestDto.getName(), taxRequestDto.getPercentage(), id);

        Tax updatedTax = taxMapper.updateTaxFromDto(taxRequestDto, existingTax);
        Tax savedTax = taxRepository.save(updatedTax);
        return taxMapper.toResponseDto(savedTax);
    }

    private void validateDuplicate(String name, Double percentage, Long excludeId) {
        List<Tax> allTaxes = taxRepository.findAll();
        String normalizedName = name.toLowerCase().replace(" ", "");

        for (Tax tax : allTaxes) {
            if (excludeId != null && tax.getId().equals(excludeId)) {
                continue;
            }
            String existingNormalizedName = tax.getName().toLowerCase().replace(" ", "");
            if (existingNormalizedName.equals(normalizedName) && tax.getPercentage().equals(percentage)) {
                throw new AlreadyExistException("Already exists");
            }
        }
    }

    @Override
    public Page<TaxResponseDto> getAllTax(Integer page, Integer size) {

        Pageable pageable;

        if (page != null && size != null) {

            if (page < 0 || size <= 0) {
                throw new IllegalArgumentException("Page must be >= 0 and size must be > 0.");
            }

            pageable = PageRequest.of(page, size);

            Page<Tax> taxPage = taxRepository.findAll(pageable);

            if (page >= taxPage.getTotalPages() && taxPage.getTotalPages() > 0) {
                throw new ResourceNotFoundException(
                        "Page " + page + " not found. Total pages: " + taxPage.getTotalPages());
            }

            return (Page<TaxResponseDto>) taxMapper.toDtoPage(taxPage);
        }

        Page<Tax> all = taxRepository.findAll(Pageable.unpaged());

        if (all.isEmpty()) {
            throw new ResourceNotFoundException("No Tax records found.");
        }

        return (Page<TaxResponseDto>) taxMapper.toDtoPage(all);
    }

    @Override
    public List<TaxResponseDto> serach(String query) {
        List<Tax> tax = taxRepository.findAll(TaxSpecs.search(query));
        return tax.stream()
                .map(taxMapper::toResponseDto)
                .toList();
    }

}
