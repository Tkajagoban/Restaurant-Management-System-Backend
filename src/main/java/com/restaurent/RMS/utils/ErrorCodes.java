package com.restaurent.RMS.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("classpath:ErrorMessages.properties")
public class ErrorCodes {

    //INVALID_PAYLOAD
    @Value("${validation.typeMismatch}")
    private String typeMismatch;

    @Value("${validation.invalidFormat}")
    private String invalidFormat;

    @Value("${validation.requiredDataMissing}")
    private String requiredDataMissing;

    //VALIDATION_FAILED
    @Value("${validation.methodArgumentNotValid}")
    private String methodArgumentNotValid;

    @Value("${validation.constraintViolation}")
    private String constraintViolation;

    //REQUIRED_FIELD_MISSING
    @Value("${validation.missingServletRequestParameter}")
    private String missingServletRequestParameter;

    @Value("${validation.missingRequestHeader}")
    private String missingRequestHeader;

    @Value("${validation.incompletePayload}")
    private String incompletePayload;

    // METHOD_NOT_ALLOWED
    @Value("${validation.httpRequestMethodNotSupported}")
    private String httpRequestMethodNotSupported;

    //UNSUPPORTED_MEDIA_TYPE
    @Value("${validation.httpMessageNotReadable}")
    private String httpMessageNotReadable;

    //FORBIDDEN
    @Value("${validation.accessDenied}")
    private String accessDenied;

    //UNAUTHORIZED
    @Value("${validation.unauthorized}")
    private String unauthorized;

    //ALREADY_EXISTS
    @Value("${validation.alreadyExist}")
    private String alreadyExist;

    //NOT_FOUND
    @Value("${validation.resourceNotFound}")
    private String resourceNotFound;

    //INVALID_STATE
    @Value("${validation.illegalArgument}")
    private String illegalArgument;

    //CONFLICT
    @Value("${validation.businessRuleViolation}")
    private String businessRuleViolation;

    //TOKEN_ERROR
    @Value("${validation.tokenExpired}")
    private String tokenExpired;

    @Value("${validation.invalidCredentials}")
    private String invalidCredentials;

    @Value("${validation.accessRevoked}")
    private String accessRevoked;

    @Value("${validation.tokenRevoked}")
    private String tokenRevoked;

    @Value("${validation.tokenExist}")
    private String tokenExist;

    @Value("${validation.tokenInvalid}")
    private String tokenInvalid;



    //DATA_INTEGRITY_ERROR
    @Value("${validation.dataIntegrityViolation}")
    private String dataIntegrityViolation;

    //INTERNAL_SERVER_ERROR
    @Value("${validation.internalServerError}")
    private String internalServerError;

    @Value("${validation.databaseError}")
    private String databaseError;

    @Value("${validation.storageError}")
    private String storageError;

    @Value("${validation.cacheError}")
    private String cacheError;


    //WRONG_API
    @Value("${validation.noHandlerFound}")
    private String noHandlerFound;

    //CONTENT_TYPE
    @Value("${validation.mediaTypeNotSupported}")
    private String mediaTypeNotSupported;

    //SQL_ERROR
    @Value("${validation.dataIntegrityError}")
    private String dataIntegrityError;



}
