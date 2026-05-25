package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.SubCategoriesRequestDto;
import com.restaurent.RMS.dtos.response.SubCategoriesResponseDto;

import com.restaurent.RMS.dtos.request.SubCategoriesRequestDto;
import com.restaurent.RMS.entities.MainCategories;
import com.restaurent.RMS.entities.SubCategories;
import com.restaurent.RMS.exceptionHandlers.AlreadyExistException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.SubCategoriesMapper;
import com.restaurent.RMS.repositories.FoodRepository;
import com.restaurent.RMS.repositories.MainCategoriesRepository;
import com.restaurent.RMS.repositories.SubCategoriesRepository;
import com.restaurent.RMS.utils.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubCategoriesServiceImpl implements SubCategoriesService {

    private final SubCategoriesRepository subCategoriesRepository;
    private final SubCategoriesMapper subCategoriesMapper;
    private final MainCategoriesRepository mainCategoriesRepository;
    private final FoodRepository foodRepository;


    @Override
    public SubCategoriesRequestDto addSubCategories(Long mainCategoryId, SubCategoriesRequestDto subCategoriesRequestDto) {
        String subCategoriesName = subCategoriesRequestDto.getSubCategoriesName();

        if (subCategoriesName == null || subCategoriesName.trim().isEmpty()) {
            throw new IllegalArgumentException("subCategories Name name cannot be empty.");
        }

        if (!subCategoriesName.equals(subCategoriesName.trim())) {
            throw new IllegalArgumentException("subCategories Name name cannot start or end with a space.");
        }

        if (!subCategoriesName.matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
            throw new IllegalArgumentException("subCategories Name name must contain only letters (A–Z).");
        }

        MainCategories mainCategories = mainCategoriesRepository.findById(mainCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("MainCategories not found with ID: " + mainCategoryId));

        boolean exists = subCategoriesRepository.existsByNameAndMainCategories_Id(
                subCategoriesName,
                mainCategoryId);
        if (exists) {
            throw new AlreadyExistException("subCategory already exists for this mainCategory.");
        }

        SubCategories subCategories = subCategoriesMapper.toSubCategoriesEntity(subCategoriesRequestDto);
        subCategories.setMainCategories(mainCategories);

        SubCategories res = subCategoriesRepository.save(subCategories);
        SubCategoriesRequestDto response = subCategoriesMapper.toResponse(res);
        response.setMainCategoryID(mainCategoryId);

        return response;
    }

    @Override
    public List<SubCategoriesResponseDto> getAllSubCategories(Long mainCategoryId) {

        if (mainCategoryId == null || mainCategoryId <= 0) {
            throw new IllegalArgumentException(ValidationMessages.RESOURCE_NOT_FOUND);
        }

        List<SubCategories> subCategories = subCategoriesRepository.findByMainCategories_Id(mainCategoryId);

        if (subCategories.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No subcategories found for mainCategoryId " + mainCategoryId
            );
        }

        return subCategories.stream()
                .map(subCategoriesMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    @Override
    public SubCategoriesResponseDto updateSubCategories (Long id, SubCategoriesRequestDto subCategoriesRequestDto)
    {
        String subCategoriesName = subCategoriesRequestDto.getSubCategoriesName();
        SubCategories subCategories = subCategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sub category not found with id : "+id));

        MainCategories mainCategories = mainCategoriesRepository.findById(
                        subCategoriesRequestDto.getMainCategoryID())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Main category not found with id : " +subCategoriesRequestDto.getMainCategoryID()));
        boolean exists = subCategoriesRepository.existsByNameAndMainCategories_IdAndIdNot(subCategoriesRequestDto.getSubCategoriesName(),subCategoriesRequestDto.getMainCategoryID(),id);
        if (exists){
            throw new AlreadyExistException("Sub category already exists");
        }

        if (!subCategoriesName.equals(subCategoriesName.trim())) {
            throw new IllegalArgumentException("subCategories Name name cannot start or end with a space.");
        }

        if (!subCategoriesName.matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
            throw new IllegalArgumentException("subCategories Name name must contain only letters (A–Z).");
        }

        subCategories.setName(subCategoriesRequestDto.getSubCategoriesName());
        subCategories.setStatus(subCategoriesRequestDto.getStatus());
        subCategories.setMainCategories(mainCategories);

        SubCategories saved = subCategoriesRepository.save(subCategories);
        // MAIN LOGIC: Ensure foods are inactive if either subcategory or main category is inactive
        if (!saved.getStatus() || !saved.getMainCategories().getStatus()) {
            foodRepository.findBySubCategories_Id(saved.getId())
                    .forEach(food -> {
                        food.setStatus(false);
                        foodRepository.save(food);
                    });
        }
        SubCategoriesResponseDto responseDto= subCategoriesMapper.toResponseDto(saved);
        // responseDto.setMainCategoryID(saved.getMainCategories().getId());
        return responseDto;

    }
    @Override
    public void subCategory_deleteById(Long id) {
        SubCategories subCategories=subCategoriesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Sub category not found"));
        subCategoriesRepository.delete(subCategories);
    }

}