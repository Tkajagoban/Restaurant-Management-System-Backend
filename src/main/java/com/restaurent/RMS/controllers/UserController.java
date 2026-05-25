package com.restaurent.RMS.controllers;
import com.restaurent.RMS.dtos.request.UserRequestDto;
import com.restaurent.RMS.dtos.response.UserResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.UserService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.SETTINGS)
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @GetMapping(EndpointBundle.USER_BY_ID)
    public ResponseEntity<ResponseWrapper<UserResponseDto>> getUserById(@PathVariable long id) {
        UserResponseDto user = userService.getUserById(id);
        ResponseWrapper<UserResponseDto> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.RETRIEVED,
                user
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(EndpointBundle.USERS_CREATE)
    public ResponseEntity<ResponseWrapper<UserResponseDto>> createUser(@PathVariable Long restaurantId, @PathVariable Long roleId, @Valid @RequestBody UserRequestDto userRequestDto ) {
        UserResponseDto createUser = userService.createUser(restaurantId,roleId,userRequestDto);
        if (createUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.SAVED_SUCCESSFULL,
                    createUser
            ));

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    ValidationMessages.RESOURCE_NOT_FOUND,
                    null
            ));
        }
    }

    @DeleteMapping(EndpointBundle.USER_BY_ID)
    public ResponseEntity<ResponseWrapper<Object>> deleteById (@PathVariable Long id){
        userService.User_deleteById(id);
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        RestApiResponseStatusCodes.DELETED.getCode(),
                        ValidationMessages.DELETE_SUCCESS,
                        null
                )
        );
    }

    @PutMapping(EndpointBundle.USER_BY_ID)
    public ResponseEntity<ResponseWrapper<UserResponseDto>> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) {

        UserResponseDto updatedUser = userService.updateUser(id, userRequestDto);

        ResponseWrapper<UserResponseDto> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.UPDATED.getCode(),
                ValidationMessages.UPDATED,
                updatedUser
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping({EndpointBundle.USER_GET})
    public ResponseEntity<ResponseWrapper<Page<UserResponseDto>>> getUsers(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserResponseDto> userPage= userService.searchUsers(restaurantId, roleId, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                RestApiResponseStatusCodes.SUCCESS.getCode(),
                ValidationMessages.RETRIEVED,
                userPage
        ));

    }

    @GetMapping(EndpointBundle.USER_SEARCH)
    public ResponseEntity<ResponseWrapper<List<UserResponseDto>>> searchUsers(@RequestParam(required = false) String query){
        List<UserResponseDto> userResponseDtos = userService.searchUser(query);

        if (userResponseDtos.isEmpty()){
            ResponseWrapper<List<UserResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                    RestApiResponseStatusCodes.NOT_FOUND.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
        } else {
            ResponseWrapper<List<UserResponseDto>> responseWrapper = new ResponseWrapper<>(
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                    RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                    userResponseDtos
            );
            return ResponseEntity.status(HttpStatus.OK).body(responseWrapper);
        }

    }
}
