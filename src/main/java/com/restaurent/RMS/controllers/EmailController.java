package com.restaurent.RMS.controllers;

import com.restaurent.RMS.dtos.request.EmailRequestDto;
import com.restaurent.RMS.dtos.response.EmailResponseDto;
import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.services.EmailService;
import com.restaurent.RMS.utils.EndpointBundle;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointBundle.SETTINGS)
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping(EndpointBundle.EMAIL)
    public ResponseEntity<ResponseWrapper<List<EmailResponseDto>>> getallEmail() {
        List<EmailResponseDto> emailResponseDtos = emailService.getAllEmail();
        ResponseWrapper<List<EmailResponseDto>> emailResponseDtoResponseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getCode(),
                RestApiResponseStatusCodes.RETRIEVED_SUCCESS.getMessage(),
                emailResponseDtos
        );
        return ResponseEntity.status(HttpStatus.OK).body(emailResponseDtoResponseWrapper);
    }


    @PostMapping(EndpointBundle.EMAIL_CREATED)
    public ResponseEntity<ResponseWrapper<EmailResponseDto>> createEmail(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        EmailResponseDto createEmail = emailService.createEmail(emailRequestDto);
        if (createEmail != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.SUCCESS.getCode(),
                    ValidationMessages.SAVED_SUCCESSFULL,
                    createEmail
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper<>(
                    RestApiResponseStatusCodes.REQUIRED_FIELD_MISSING.getCode(),
                    ValidationMessages.SAVE_FAILED,
                    null
            ));
        }

    }


    @PutMapping(EndpointBundle.EMAIL_BY_ID)
    public ResponseEntity<ResponseWrapper<EmailResponseDto>> updateEmail(@PathVariable Long id, @Valid @RequestBody EmailRequestDto emailRequestDto) {

        EmailResponseDto updateEmail = emailService.updateEmail(id, emailRequestDto);

        ResponseWrapper<EmailResponseDto> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.UPDATED.getCode(),
                ValidationMessages.UPDATED,
                updateEmail
        );
        return ResponseEntity.ok(response);

    }
}