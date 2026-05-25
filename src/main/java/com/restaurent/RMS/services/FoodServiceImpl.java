package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.FoodRequestDto;
import com.restaurent.RMS.dtos.response.FoodResponseDto;
import com.restaurent.RMS.dtos.response.MainCategoriesResponseDto;
import com.restaurent.RMS.entities.Food;
import com.restaurent.RMS.entities.MainCategories;
import com.restaurent.RMS.entities.SubCategories;
import com.restaurent.RMS.exceptionHandlers.AlreadyExistException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.FoodMapper;
import com.restaurent.RMS.repositories.FoodRepository;
import com.restaurent.RMS.repositories.MainCategoriesRepository;
import com.restaurent.RMS.repositories.SubCategoriesRepository;
import com.restaurent.RMS.specification.FoodSpecs;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final MainCategoriesRepository mainCategoriesRepository;
    private final SubCategoriesRepository subCategoriesRepository;
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;

    @Override
    public Page<FoodResponseDto> getAllFood(Long mainCategoryId, Long subcategoryId, int page, int size) {
        if (page < 0 || size < 0 || (mainCategoryId != null && mainCategoryId < 0)
                || (subcategoryId != null && subcategoryId < 0)) {
            throw new ConstraintViolationException(ValidationMessages.CONSTRAINT_VIOLATION, null);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Food> foodpage;

        if (mainCategoryId == null && subcategoryId == null) {
            // No filter, return all foods
            foodpage = foodRepository.findAll(pageable);
        } else if (mainCategoryId != null && subcategoryId == null) {
            // Filter only by main category
            foodpage = foodRepository.findByMainCategories_Id(mainCategoryId, pageable);
        } else if (mainCategoryId == null && subcategoryId != null) {
            // Filter only by subcategory
            foodpage = foodRepository.findBySubCategories_Id(subcategoryId, pageable);
        } else {
            // Filter by both main and sub category
            foodpage = foodRepository.findByMainCategories_IdAndSubCategories_Id(mainCategoryId, subcategoryId,
                    pageable);
        }

        if (foodpage.isEmpty()) {
            throw new ResourceNotFoundException("No food found for the given filters");
        }

        return foodpage.map(foodMapper::toDto);
    }

    private String buildMergedFoodName(MainCategories mainCategory, String foodName) {
        if (mainCategory == null || mainCategory.getName() == null) {
            return foodName;
        }
        String categoryName = mainCategory.getName().trim();
        String trimmedFoodName = foodName.trim();

        // Check if the food name already starts with the category name
        // (case-insensitive check for robustness)
        if (trimmedFoodName.toLowerCase().startsWith(categoryName.toLowerCase() + " ")) {
            return trimmedFoodName;
        }

        return categoryName + " " + trimmedFoodName;
    }

    @Override
    public FoodResponseDto createFood(Long mainCategoriesId, Long subCategoriesId, FoodRequestDto foodRequestDto) {
        MainCategories mainId = mainCategoriesRepository.findById(mainCategoriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Maincategories id not found"));

        SubCategories subId = subCategoriesRepository.findById(subCategoriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategories id not found"));

        // Build merged name: Category Name + Food Name
        String finalName = buildMergedFoodName(mainId, foodRequestDto.getName());

        boolean exists = foodRepository.existsByNameAndMainCategories_IdAndSubCategories_Id(
                finalName, mainCategoriesId, subCategoriesId);
        if (exists) {
            throw new IllegalArgumentException("Food name already exists in this category");
        }

        Food createdfood = foodMapper.toEntity(foodRequestDto);
        createdfood.setName(finalName); // Set the merged name
        createdfood.setMainCategories(mainId);
        createdfood.setSubCategories(subId);
        Food savedFood = foodRepository.save(createdfood);

        return foodMapper.toDto(savedFood);

    }

    @Override
    public void deletebyid(Long id) {
        foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("food id not found : " + id));
        foodRepository.deleteById(id);

    }

    @Override

    public FoodResponseDto getfood(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(ValidationMessages.CONSTRAINT_VIOLATION);

        }
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ValidationMessages.RESOURCE_NOT_FOUND));

        return foodMapper.toDto(food);

    }

    @Override
    public FoodResponseDto updateFood(FoodRequestDto foodRequestDto, Long id) {

        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food id " + id + " is not found"));

        // Validate generic name format first (letters and spaces)
        String nameRegex = "^[A-Za-z]+( [A-Za-z]+)*$";
        if (!foodRequestDto.getName().matches(nameRegex)) {
            throw new IllegalArgumentException("Food name must contain only letters and single spaces between words");
        }

        Long mainCategoryId = food.getMainCategories().getId();
        Long subCategoryId = food.getSubCategories().getId();

        // Build merged name: Category Name + Input Name
        String finalName = buildMergedFoodName(food.getMainCategories(), foodRequestDto.getName());

        boolean exists = foodRepository
                .existsByNameAndMainCategories_IdAndSubCategories_IdAndIdNot(
                        finalName,
                        mainCategoryId,
                        subCategoryId,
                        id);

        if (exists) {
            throw new IllegalArgumentException(
                    "Food name already exists in this main category and sub category");
        }

        food.setName(finalName); // Update with merged name
        food.setPrice(foodRequestDto.getPrice());
        food.setStatus(foodRequestDto.getStatus());

        if (foodRequestDto.getImage() != null && !foodRequestDto.getImage().isEmpty()) {
            food.setImage(foodRequestDto.getImage());
        }

        Food updated = foodRepository.save(food);

        return foodMapper.toFoodResponseDto(updated);
    }

    @Override
    public List<FoodResponseDto> searchFood(String query) {
        List<Food> foods = foodRepository.findAll(FoodSpecs.search(query));
        return foods.stream()
                .map(foodMapper::toDto)
                .toList();
    }

}
