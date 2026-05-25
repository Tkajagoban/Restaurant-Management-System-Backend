package com.restaurent.RMS.services;

import com.restaurent.RMS.dtos.request.MainCategoriesRequestDto;
import com.restaurent.RMS.dtos.response.MainCategoriesResponseDto;
import com.restaurent.RMS.entities.MainCategories;
import com.restaurent.RMS.entities.Restaurant;
import com.restaurent.RMS.entities.SubCategories;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.mappers.MainCategoriesMapper;
import com.restaurent.RMS.repositories.FoodRepository;
import com.restaurent.RMS.repositories.MainCategoriesRepository;
import com.restaurent.RMS.exceptionHandlers.AlreadyExistException;
import com.restaurent.RMS.exceptionHandlers.RequiredDataMissingException;
import com.restaurent.RMS.exceptionHandlers.ResourceNotFoundException;
import com.restaurent.RMS.repositories.SubCategoriesRepository;
import com.restaurent.RMS.utils.ValidationMessages;
import com.restaurent.RMS.repositories.RestaurantRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainCategoriesServiceImpl implements MainCategoriesService {
    public final MainCategoriesRepository mainCategoriesRepository;
    public final MainCategoriesMapper mainCategoriesMapper;
    public final RestaurantRepository restaurantRepository;
    public final SubCategoriesRepository subCategoriesRepository;
    public final FoodRepository foodRepository;


    @Override
    public MainCategoriesRequestDto updateMainCategory(Long id, MainCategoriesRequestDto requestDto) {
        MainCategories existCategory = mainCategoriesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(ValidationMessages.RESOURCE_NOT_FOUND));

        Long restaurantId = existCategory.getRestaurant().getId();
        String categoryName = requestDto.getName();


        boolean alreadyExists = mainCategoriesRepository
                .existsByRestaurant_IdAndNameIgnoreCaseAndIdNot(restaurantId, categoryName, id);

        if (alreadyExists) {
            throw new ResourceNotFoundException(ValidationMessages.ALREADY_EXISTS);
        }

        mainCategoriesMapper.updateEntityFromDto(requestDto, existCategory);
        MainCategories updated = mainCategoriesRepository.save(existCategory);

        // --- CASCADE STATUS LOGIC ---
        boolean newStatus = updated.getStatus();

        List<SubCategories> subCategories = subCategoriesRepository.findByMainCategories_Id(updated.getId());
        for (SubCategories sub : subCategories) {
            // Update SubCategory status to match MainCategory
            sub.setStatus(newStatus);
            subCategoriesRepository.save(sub);

            // Update all foods under this SubCategory to match MainCategory status
            foodRepository.findBySubCategories_Id(sub.getId()).forEach(food -> {
                food.setStatus(newStatus);
                foodRepository.save(food);
            });
        }
        MainCategoriesRequestDto mainCategoriesRequestDto= mainCategoriesMapper.toDto(updated);
        mainCategoriesRequestDto.setRestaurantId(updated.getRestaurant().getId());
        return  mainCategoriesRequestDto;
    }
    @Override
    public void mainCategory_deleteById(Long id) {
        MainCategories mainCategories = mainCategoriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Main Category not found with id:"+id));

        mainCategoriesRepository.delete(mainCategories);

    }

    //kanujan write createMainCategory
    @Override
    public MainCategoriesResponseDto createMainCategory(Long restaurantId, MainCategoriesRequestDto mainCategoriesRequestDto) {
        // handle Required fields are missing
        if(mainCategoriesRequestDto == null
                || mainCategoriesRequestDto.getName()== null || mainCategoriesRequestDto.getName().isBlank()
                || mainCategoriesRequestDto.getStatus() == null){
            throw new RequiredDataMissingException("Required fields are missing: name or Status");
        }

        //handle name validation
        String name = mainCategoriesRequestDto.getName();
        if (!name.matches("^[A-Za-z ]+$")) {
            throw new RequiredDataMissingException("Name must contain only letters and spaces");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new RequiredDataMissingException("Name length must be between 2 and 100 characters");
        }

        // check Restaurant id
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new ResourceNotFoundException("Restaurant not found with id"+restaurantId));

        //handle Main category already exists or not
        boolean exists = mainCategoriesRepository.existsByNameAndRestaurantId(mainCategoriesRequestDto.getName(), restaurantId);
        if (exists) {
            throw new AlreadyExistException("Main category already exists for this restaurant.");
        }

        MainCategories mainCategories = mainCategoriesMapper.toEntity(mainCategoriesRequestDto);
        mainCategories.setRestaurant(restaurant);
        MainCategories savedMainCategory = mainCategoriesRepository.save(mainCategories);
        return mainCategoriesMapper.toResponseDto(savedMainCategory);
    }

    @Override
    public Page<MainCategoriesResponseDto> getAll(Integer page, Integer size) {

        Pageable pageable;

        if (page != null && size != null) {

            if (page < 0 || size <= 0) {
                throw new IllegalArgumentException("Page and size must be positive");
            }

            pageable = PageRequest.of(page, size);

            Page<MainCategories> pageResult =
                    mainCategoriesRepository.findAll(pageable);


            if (page >= pageResult.getTotalPages() && pageResult.getTotalPages() > 0) {
                throw new ResourceNotFoundException("Page number not found");
            }

            return mainCategoriesMapper.toDtoPage(pageResult);
        }


        Page<MainCategories> all =
                mainCategoriesRepository.findAll(Pageable.unpaged());


        if (all.isEmpty()) {
            throw new ResourceNotFoundException("Main categories not found");
        }

        return mainCategoriesMapper.toDtoPage(all);


    }

    @Override
    public MainCategoriesResponseDto getMainCategory(Long id ){
        if (id < 0) {
            throw new IllegalArgumentException(ValidationMessages.CONSTRAINT_VIOLATION);

        }
        MainCategories mainCategories=mainCategoriesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException( ValidationMessages.RESOURCE_NOT_FOUND));
        MainCategoriesResponseDto mainCategoriesResponseDto=mainCategoriesMapper.toResponseDto(mainCategories);

        return mainCategoriesResponseDto;

    }
}
