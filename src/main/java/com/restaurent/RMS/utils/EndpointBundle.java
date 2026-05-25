package com.restaurent.RMS.utils;

public class EndpointBundle {

    // Base URL
    public static final String BASE_URL = "/api/v1";
    public static final String ID = "/{id}";
    public static final String RESTAURANT_ID = "/{restaurantId}";
    public static final String ROLE_ID = "/{roleId}";
    public static final String SEARCH = "/search";
    public static final String MAIN_CATEGORY_ID = "/{mainCategoryId}";
    public static final String SUB_CATEGORY_ID = "/{subCategoryId}";

    // Settings Base
    public static final String SETTINGS = BASE_URL + "/settings";

    // RESTAURANT
    public static final String RESTAURANTS = SETTINGS + "/restaurant";
    public static final String RESTAURANTS_CREATE = RESTAURANTS + "/added";
    public static final String RESTAURANTS_BY_ID = RESTAURANTS + ID;

    // ROLE Controller
    public static final String ROLE = "/roles";
    public static final String ROLES = RESTAURANT_ID + "/roles";
    public static final String ROLES_CREATE = ROLES + "/added";
    public static final String ROLE_BY_ID = ROLE + ID;
    public static final String ROLE_SEARCH = ROLE + SEARCH;

    // USER Controller
    public static final String USER = "/users";
    public static final String USERS = RESTAURANT_ID + ROLE_ID + USER;
    public static final String USERS_CREATE = USERS + "/added";
    public static final String USER_BY_ID = USER + ID;
    public static final String USER_GET = RESTAURANT_ID + USER;
    public static final String USER_SEARCH = USER + SEARCH;

    // EMAIL Controller
    public static final String EMAIL = "/email";
    public static final String EMAIL_CREATED = EMAIL + "/added";
    // public static final String EMAIL_BY_ID = EMAIL+ID;
    public static final String EMAIL_BY_ID = EMAIL + ID;

    // MAIN CATEGORIES Controller
    public static final String MAIN_CATEGORIES = "/MainCategories";
    public static final String MAIN_CATEGORIES_CREATE = RESTAURANT_ID + MAIN_CATEGORIES + "/added";
    public static final String MAIN_CATEGORIES_BY_ID = MAIN_CATEGORIES + ID;

    // SUB CATEGORIES Controller
    public static final String SUB_CATEGORY = "/SubCategory";
    public static final String SUB_CATEGORIES = MAIN_CATEGORY_ID + "/SubCategory";
    public static final String SUB_CATEGORIES_BY_ID = SUB_CATEGORY + ID;
    public static final String SUB_CATEGORY_CREATE = SUB_CATEGORIES + "/added";

    // AUTH / LOGIN
    public static final String AUTH = BASE_URL + "/auth";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String FORGOT_PASSWORD = "/forgot-password";
    public static final String OTP = "/otp";
    public static final String VERIFY_OTP = OTP + "/verify";
    public static final String NEW_PASSWORD = "/new-password";

    // TAX
    public static final String TAX = "/tax";
    public static final String TAX_ADDED = TAX + "/added";
    public static final String TAX_ID = TAX + ID;
    public static final String TAX_SEARCH = TAX + SEARCH;

    // FOOD
    public static final String FOOD = "/food";
    public static final String FOOD_ADDED = MAIN_CATEGORY_ID + SUB_CATEGORY_ID + FOOD + "/added";
    public static final String FOODS = MAIN_CATEGORY_ID + SUB_CATEGORY_ID + "/food";
    public static final String FOOD_BY_ID = FOOD + ID;
    public static final String FOOD_UPDATE = FOOD + ID;
    public static final String FOOD_SEARCH = FOOD + SEARCH;

    // TABLE
    public static final String TABLE = "/table";
    public static final String TABLE_ADDED = TABLE + "/added";
    public static final String TABLE_ID = TABLE + ID;
    public static final String TABLE_SEARCH = TABLE + SEARCH;

    // OrderSummary
    public static final String ORDER_SUMMARY = "/ordersummary";
    public static final String ORDER_SUMMARY_ADD = ORDER_SUMMARY + "/added";

    // OrderSummary
    public static final String ORDER_SUMMARY_BY_ID = ORDER_SUMMARY + ID;
    public static final String ORDERS = "/orders";

    // RESTAURANT_PRIVILEGE
    public static final String RESTAURANT_PRIVILEGE = RESTAURANT_ID + "/restaurantPrivilege";
    public static final String RESTAURANT_PRIVILEGE_ADDED = RESTAURANT_PRIVILEGE + "/added";
    public static final String RESTAURANT_PRIVILEGE_GETALL = RESTAURANT_PRIVILEGE;

    // ORDER_MANAGEMENT
    public static final String ORDER_MANAGEMENT = "/orderManagement";
    public static final String ORDER_MANAGEMENT_SEARCH = ORDER_MANAGEMENT + SEARCH;

    // INVOICE
    public static final String INVOICE = "/invoice";
    public static final String INVOICES = INVOICE;

    // ROLE_PRIVILEGE
    public static final String ROLE_PRIVILEGE = "/rolePrivilege";
    public static final String ROLE_PRIVILEGE_GET_ALL = SETTINGS + RESTAURANT_ID + ROLE_PRIVILEGE;
    public static final String ROLE_PRIVILEGE_BY_ID = ROLE_PRIVILEGE + ID;
    public static final String ROLE_PRIVILEGE_BY_ROLE_ID = ROLE_PRIVILEGE + "/role" + ROLE_ID;

}
