package com.restaurent.RMS.utils;

public class ValidationMessages {

    // GENERAL
    public static final String SUCCESS = "Success";
    public static final String SAVED_SUCCESSFULL = "Saved successfully.";
    public static final String SAVE_FAILED = "Save failed.";
    public static final String UPDATED = "Updated successfully.";
    public static final String UPDATED_FAILED = "Update failed.";
    public static final String DELETE_SUCCESS = "Deleted successfully.";
    public static final String DELETE_FAILED = "Delete failed.";
    public static final String RETRIEVED = "Retrieved successfully.";
    public static final String RETRIEVED_FAILED = "Retrieve failed.";

    public static final String INTERNAL_SERVER_ERROR = "An unexpected server error occurred";

    // VALIDATION & PAYLOAD ERRORS
    public static final String METHOD_ARGUMENT_NOT_VALID = "One or more fields have invalid values";
    public static final String CONSTRAINT_VIOLATION = "Request parameters failed validation";
    public static final String TYPE_MISMATCH = "Invalid data type provided";
    public static final String INVALID_FORMAT = "Incorrect data format in request body";
    public static final String INCOMPLETE_PAYLOAD = "Mandatory fields are missing in request";

    // SPECIFIC FIELD VALIDATIONS
    public static final String EMAIL_NOT_EMPTY = "Email address is required";
    public static final String INVALID_NAME = "Name can contain only alphabetic characters (A-Z, a-z).";
    public static final String NAME_LEADING_SPACE = "Name must not start with a space.";
    public static final String NAME_TRAILING_SPACE = "Name must not end with a space.";
    public static final String INVALID_EMAIL = "Email must be valid (example: your-email@example.com).";
    public static final String INVALID_ID = "Invalid ID: No value present.";
    public static final String INVALID_INPUT = "Input data is not valid. Please check the fields.";
    public static final String MISMATCH_INPUT = "Input is not in a valid format.";
    public static final String MISMATCH_FIELD = "Required field is missing.";
    public static final String MIN_REQUIREMENT = "At least one attribute must be present.";
    public static final String INVALID_ID_VALUE = "ID must be a positive number";
    public static final String NAME_SIZE = "name must be between 2 and 100 characters";
    public static final String STATUS = "Status is required";
    public static final String MIN_PORT = "Port must be greater than 0";

    // REQUIRED FIELD MISSING
    public static final String MISSING_REQUEST_PARAMETER = "Required request parameter is missing";
    public static final String MISSING_REQUEST_HEADER = "Required request header is missing";
    public static final String MISSING_PARAMETER = "Missing parameter.";
    public static final String REQUIRED_DATA_MISSING = "Required field missing";

    // HTTP ERRORS
    public static final String METHOD_NOT_ALLOWED = "HTTP method not allowed";
    public static final String UNSUPPORTED_MEDIA_TYPE = "Request payload is unreadable or malformed";
    public static final String WRONG_API_CALL = "Wrong API method or path. Ensure your API path and method are correct.";
    public static final String WRONG_JSON = "JSON is not in a valid format.";
    public static final String INVALID_URL = "Invalid URL endpoint.";

    // BUSINESS EXCEPTIONS
    public static final String ALREADY_EXISTS = "Resource already exists";
    public static final String RESOURCE_NOT_FOUND = "Requested resource not found";
    public static final String INVALID_ARGUMENT = "Invalid argument provided";
    public static final String BUSINESS_RULE_VIOLATION = "Business rule violation occurred";
    public static final String DUPLICATE_ENTRY = "Duplicate entry: Data already exists in database.";

    // AUTH ERRORS
    public static final String ACCESS_DENIED = "Access denied for this resource";
    public static final String NOT_ACCESS = "NOT ACCESS";
    public static final String UNAUTHORIZED = "Authentication failed or unauthorized";

    // TOKEN ERROR
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_REVOKED = "Token has been revoked";
    public static final String TOKEN_INVALID_ = "Invalid token ";
    public static final String TOKEN_EXIST = "Token expired and login again";
    public static final String INVALID_CREDENTIALS = "Invalid login credentials";
    public static final String ACCESS_REVOKED = "Access has been revoked";

    // DATA INTEGRITY ERRORS
    public static final String DATA_INTEGRITY_VIOLATION = "Data integrity violation occurred";
    public static final String FOREIGN_KEY_CONSTRIN = "Unable to delete: This record is linked to another record.";

    // CONTENT_TYPE
    public static final String MEDIA_TYPE_NOT_SUPPORTED = "Request Content-Type not supported";

    // Login
    public static final String INVALID_CREDENTIAL = "Invalid email or password";
    public static final String PASSWORD_NOT_EMPTY = "Password is required";

    // Password
    public static final String PASSWORD_REQUIRED = "Password is required and must be at least 8 characters.";
    public static final String PASSWORD_MIN_LENGTH = "Password must be at least 8 characters.";
    public static final String PASSWORD_COMPLEXITY = "Password must contain uppercase, lowercase, number, and special character";
    public static final String PASSWORD_MISMATCH = "New password and confirm password do not match";
    public static final String NEW_PASSWORD_SAME_AS_OLD = "New password cannot be the same as the old password";
}
