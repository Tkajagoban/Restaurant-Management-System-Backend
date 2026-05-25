package com.restaurent.RMS.exceptionHandlers;

import com.restaurent.RMS.enums.RestApiResponseStatusCodes;
import com.restaurent.RMS.utils.ErrorCodes;
import com.restaurent.RMS.utils.ErrorDetail;
import com.restaurent.RMS.utils.ResponseWrapper;
import com.restaurent.RMS.utils.ValidationMessages;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorCodes errorCodes;

    //Request body validation failed (@Valid, @NotNull, @Size, @Email)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<List<ErrorDetail>>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ErrorDetail(new Date(),
                        err.getField() + ": " +err.getDefaultMessage(),
                        errorCodes.getMethodArgumentNotValid()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ResponseWrapper<>(
                RestApiResponseStatusCodes.VALIDATION_FAILED.getCode(),
                RestApiResponseStatusCodes.VALIDATION_FAILED.getMessage(),
                details
        ));

    }
    //Path variable / query param validation failed
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseWrapper<?>> handleConstraintViolation(ConstraintViolationException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                e.getMessage(),
                errorCodes.getConstraintViolation());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.VALIDATION_FAILED.getCode(),
                RestApiResponseStatusCodes.VALIDATION_FAILED.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //URL parameter type mismatch (string instead of int)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseWrapper<?>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                ValidationMessages.TYPE_MISMATCH,
                errorCodes.getTypeMismatch());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.INVALID_PAYLOAD.getCode(),
                RestApiResponseStatusCodes.INVALID_PAYLOAD.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //Invalid data format (email, date, phone, number)
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ResponseWrapper<?>> handleInvalidFormat(InvalidFormatException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                e.getMessage(),
                errorCodes.getInvalidFormat());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.INVALID_PAYLOAD.getCode(),
                RestApiResponseStatusCodes.INVALID_PAYLOAD.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Required query parameter missing
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseWrapper<?>> handleMissingRequestParam(MissingServletRequestParameterException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                ValidationMessages.MISSING_REQUEST_PARAMETER,
                errorCodes.getMissingServletRequestParameter());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.REQUIRED_FIELD_MISSING.getCode(),
                RestApiResponseStatusCodes.REQUIRED_FIELD_MISSING.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //Required header missing (e.g., Authorization)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ResponseWrapper<?>> handleMissingHeader(MissingRequestHeaderException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                ValidationMessages.MISSING_REQUEST_HEADER,
                errorCodes.getMissingRequestHeader());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.REQUIRED_FIELD_MISSING.getCode(),
                RestApiResponseStatusCodes.REQUIRED_FIELD_MISSING.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //Entire payload missing/Some mandatory fields missing
    @ExceptionHandler(RequiredDataMissingException.class)
    public ResponseEntity<ResponseWrapper<?>> handleRequiredDataMissing(RequiredDataMissingException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                e.getMessage(),
                errorCodes.getRequiredDataMissing());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.INVALID_PAYLOAD.getCode(),
                RestApiResponseStatusCodes.INVALID_PAYLOAD.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //Wrong HTTP method (POST used instead of GET)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseWrapper<?>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                e.getMessage(),
                errorCodes.getHttpRequestMethodNotSupported());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.METHOD_NOT_ALLOWED.getCode(),
                RestApiResponseStatusCodes.METHOD_NOT_ALLOWED.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    //Malformed JSON / unreadable request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseWrapper<?>> handleUnreadable(HttpMessageNotReadableException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                ValidationMessages.UNSUPPORTED_MEDIA_TYPE,
                errorCodes.getHttpMessageNotReadable());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.UNSUPPORTED_MEDIA_TYPE.getCode(),
                RestApiResponseStatusCodes.UNSUPPORTED_MEDIA_TYPE.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

 //User doesn’t have permission (authorization failure)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseWrapper<?>> handleAccessDenied(AccessDeniedException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                ValidationMessages.ACCESS_DENIED,
                errorCodes.getAccessDenied());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.FORBIDDEN.getCode(),
                RestApiResponseStatusCodes.FORBIDDEN.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // Missing / invalid / expired token
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseWrapper<?>> handleUnauthorized(UnauthorizedException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                ValidationMessages.UNAUTHORIZED,
                errorCodes.getUnauthorized());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.UNAUTHORIZED.getCode(),
                RestApiResponseStatusCodes.UNAUTHORIZED.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    //Resource already exists
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ResponseWrapper<?>> handleAlreadyExist(AlreadyExistException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                e.getMessage(),
                errorCodes.getAlreadyExist());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.ALREADY_EXISTS.getCode(),
                RestApiResponseStatusCodes.ALREADY_EXISTS.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Resource not found using id
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<?>> handleNotFound(ResourceNotFoundException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                e.getMessage(),
                errorCodes.getResourceNotFound());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.NOT_FOUND.getCode(),
                RestApiResponseStatusCodes.NOT_FOUND.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Invalid argument passed manually
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseWrapper<?>> handleIllegalArgument(IllegalArgumentException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                e.getMessage(),
                errorCodes.getIllegalArgument());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.INVALID_STATE.getCode(),
                RestApiResponseStatusCodes.INVALID_STATE.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //Business logic rule failed
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ResponseWrapper<?>> handleBusinessRule(BusinessRuleViolationException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                e.getMessage(),
                errorCodes.getBusinessRuleViolation());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.CONFLICT.getCode(),
                RestApiResponseStatusCodes.CONFLICT.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseWrapper<?>> handleBadCredentials (BadCredentialsException e){
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(), ValidationMessages.INVALID_CREDENTIALS, errorCodes.getInvalidCredentials());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.INVALID_CREDENTIALS.getCode(),
                RestApiResponseStatusCodes.INVALID_CREDENTIALS.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseWrapper);
    }


    @ExceptionHandler(AccessRevokedException.class)
    public ResponseEntity<ResponseWrapper<?>> handleAccessRevoked (AccessRevokedException e){
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(), ValidationMessages.ACCESS_REVOKED, errorCodes.getAccessRevoked());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.ACCESS_REVOKED.getCode(),
                RestApiResponseStatusCodes.ACCESS_REVOKED.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseWrapper);
    }
    @ExceptionHandler(TokenRevokedException.class)
    public ResponseEntity<ResponseWrapper<?>> handleTokenRevoked(TokenRevokedException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(), ValidationMessages.TOKEN_REVOKED, errorCodes.getAccessRevoked());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.ACCESS_REVOKED.getCode(),
                ValidationMessages.TOKEN_REVOKED,
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseWrapper);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseWrapper<?>> handleNoHandlerFound (NoHandlerFoundException e){
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(), ValidationMessages.WRONG_API_CALL, errorCodes.getNoHandlerFound());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.NO_HANDLER_FOUND.getCode(),
                RestApiResponseStatusCodes.NO_HANDLER_FOUND.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseWrapper);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseWrapper<?>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(
                new Date(),
                ValidationMessages.MEDIA_TYPE_NOT_SUPPORTED,
                errorCodes.getMediaTypeNotSupported()
        );
        errorDetails.add(errorDetail);

        ResponseWrapper<?> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.MEDIA_TYPE_NOT_SUPPORTED.getCode(),
                RestApiResponseStatusCodes.MEDIA_TYPE_NOT_SUPPORTED.getMessage(),
                errorDetails
        );

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(responseWrapper);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseWrapper<?>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(
                new Date(),
                ValidationMessages.DATA_INTEGRITY_VIOLATION,
                errorCodes.getDataIntegrityError()
        );
        errorDetails.add(errorDetail);

        ResponseWrapper<?> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.DATA_INTEGRITY_ERROR.getCode(),
                RestApiResponseStatusCodes.DATA_INTEGRITY_ERROR.getMessage(),
                errorDetails
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseWrapper);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ResponseWrapper<?>> handleTokenExpired(TokenExpiredException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(new Date(),
                e.getMessage(),
                errorCodes.getUnauthorized());
        errorDetails.add(errorDetail);

        ResponseWrapper<?> response = new ResponseWrapper<>(
                RestApiResponseStatusCodes.UNAUTHORIZED.getCode(),
                RestApiResponseStatusCodes.UNAUTHORIZED.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @ExceptionHandler(TokenAlreadyExistException.class)
    public ResponseEntity<ResponseWrapper<?>> handleTokenAlreadyExist(TokenAlreadyExistException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(
                new Date(),
                e.getMessage() != null ? e.getMessage() : ValidationMessages.TOKEN_EXIST,
                errorCodes.getTokenExist() // 40025
        );
        errorDetails.add(errorDetail);

        ResponseWrapper<?> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.ALREADY_EXISTS.getCode(),
                ValidationMessages.TOKEN_EXIST,
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseWrapper);
    }

    // Token invalid
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ResponseWrapper<?>> handleTokenInvalid(TokenInvalidException e) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        ErrorDetail errorDetail = new ErrorDetail(
                new Date(),
                e.getMessage() != null ? e.getMessage() : ValidationMessages.TOKEN_INVALID_,
                errorCodes.getTokenInvalid()
        );
        errorDetails.add(errorDetail);

        ResponseWrapper<?> responseWrapper = new ResponseWrapper<>(
                RestApiResponseStatusCodes.UNAUTHORIZED.getCode(),
                RestApiResponseStatusCodes.UNAUTHORIZED.getMessage(),
                errorDetails
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseWrapper);
    }



}
