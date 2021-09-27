package com.app.util;

/**
 * This class contains the CONSTANTS for this project
 */
public class ApiConstants {

    // Base URL
    public static final String BASE_URL = "https://api.themoviedb.org/4";

    // Credential
    // TODO: Enter these values
    public static final String API_KEY = "?api_key="; // e.g. Enter the api_key: ?api_key=wqew24324
    public static final String ACCESS_BEARER_TOKEN = ""; // enter the Bearer token with write access

    // Request & Reponse Body Data
    public static final String PARAM_NAME = "My favorite movie list";
    public static final String UPDATED_LIST_NAME = "My updated favorite movie list";
    public static final String DESCRIPTION = "I would like to add the Horror, Animated, Suspense and Action Genres in this list";
    public static final String ISO_639_1 = "en";
    public static final String STATUS_CODE_1 = "1";
    public static final String STATUS_CODE_12 = "12";
    public static final String STATUS_CODE_13 = "13";
    public static final String THE_ITEM_WAS_CREATED_SUCCESSFULLY = "The item/record was created successfully.";
    public static final String THE_ITEM_WAS_UPDATED_SUCCESSFULLY = "The item/record was updated successfully.";
    public static final String THE_ITEM_WAS_DELETED_SUCCESSFULLY = "The item/record was deleted successfully.";
    public static final String RESOURCE_NOT_FOUND = "The resource you requested could not be found.";
    public static final String INVALID_API_KEY = "Invalid API key: You must be granted a valid key.";
    public static final String INTERNAL_ERROR = "Internal error: Something went wrong, contact TMDb.";
    public static final String TRUE = "true";
    public static final String SUCCESS = "Success.";
    public static final String MOVIE_550_COMMENT = "New comment added to the list!";
    public static final String MEDIA_TYPE_550 = "550";
    public static final String MEDIA_TYPE_244786 = "244786";
    public static final String MEDIA_TYPE_1396 = "1396";

    // HTTP Status Codes
    public static final int CODE_200 = 200;
    public static final int CODE_201 = 201;
    public static final int CODE_400 = 400;
    public static final int CODE_401 = 401;
    public static final int CODE_422 = 422;
    public static final int CODE_404 = 404;
    public static final int CODE_500 = 500;

}
