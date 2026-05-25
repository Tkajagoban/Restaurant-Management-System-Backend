package com.restaurent.RMS.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RestApiResponseStatusCodes {
    SUCCESS(2000, "Success"),
    NO_CHANGES(2000, "No changes"),
    UPDATED(2000, "Updated"),
    CREATED(2001, "Created"),
    ACCEPTED(2002, "Accepted"),
    PROCESSING(2003, "Processing"),
    NO_CONTENT(2004, "No Content"),
    DELETED(2004, "Deleted"),
    PARTIAL_SUCCESS(2006, "Partial Success"),
    ALREADY_EXISTS(2007, "Already Exists"),
    RETRIEVED_SUCCESS(2008,"Retrieved successfully"),

    BAD_REQUEST(4000, "Bad Request"),
    UNAUTHORIZED(4001, "Unauthorized"),
    FORBIDDEN(4003, "Forbidden"),
    NOT_FOUND(4004, "Not Found"),
    METHOD_NOT_ALLOWED(4005, "Method Not Allowed"),
    INVALID_PAYLOAD(4006, "Invalid Payload"),
    TIMEOUT(4008, "Request Timeout"),
    CONFLICT(4009, "Conflict"),

    UNSUPPORTED_MEDIA_TYPE(4015, "Unsupported Media Type"),
    INVALID_CREDENTIALS(4016, "Invalid Credentials"),
    TOKEN_EXPIRED(4017, "Token Expired"),
    ACCESS_REVOKED(4018, "Access Revoked"),
    DUPLICATE_ENTRY(4019, "Duplicate Entry"),
    REQUIRED_FIELD_MISSING(4020, "Required Field Missing"),
    INVALID_STATE(4021, "Invalid State"),
    VALIDATION_FAILED(4022, "Validation Failed"),
    DATA_INTEGRITY_ERROR(4023, "Data Integrity Error"),
    NO_HANDLER_FOUND(4024,"Wrong api call"),
    MEDIA_TYPE_NOT_SUPPORTED(4025,"Content-Type not supported"),

    INTERNAL_SERVER_ERROR(5000, "Internal Server Error"),
    BAD_GATEWAY(5002, "Bad Gateway"),
    SERVICE_UNAVAILABLE(5003, "Service Unavailable"),
    GATEWAY_TIMEOUT(5004, "Gateway Timeout"),
    DATABASE_ERROR(5005, "Database Error"),
    DEPENDENCY_FAILURE(5006, "Dependency Failure"),
    CONFIG_ERROR(5007, "Configuration Error"),
    STORAGE_ERROR(5008, "Storage Error"),
    CACHE_ERROR(5009, "Cache Error");


    private final int code;
    private final String message;
}
