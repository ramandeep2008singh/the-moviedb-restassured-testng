package com.app.test;

import com.app.util.ApiConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * @author rsinghnagi
 * This class contains negative tests for Movie DB
 */
public class MovieListNegitiveTests extends TestBase {

    @Test(description = "Send a POST request with invalid Bearer token")
    public void postListWithFalseBearerTokenTest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "My favorite movie list");
        requestParams.put("iso_639_1", "en");

        Response response = given()
                .header("Authorization", "Bearer 123455")
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .and()
                .body(requestParams.toJSONString())
                .when()
                .post("/list")
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), ApiConstants.CODE_401);
    }

    @Test(description = "POST request with an empty body")
    public void postListWithoutRequestBodyTest() {
        Response response = given()
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .and()
                .when()
                .post("/list")
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), ApiConstants.CODE_422);
    }

    @Test(description = "DELETE the already deleted List=7109529")
    public void deleteSameListTwiceTest() {
        Response response = given()
                .urlEncodingEnabled(false)
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json; charset=UTF-8")
                .and()
                .when()
                .delete("/list/7109529" + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), ApiConstants.CODE_404);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                            ApiConstants.RESOURCE_NOT_FOUND);
    }

    @Test(description = "POST request without Authorization")
    public void sendRequestWithoutAuthTest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "My favorite movie list");
        requestParams.put("iso_639_1", "en");

        Response response = given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .and()
                .body(requestParams.toJSONString())
                .when()
                .post("/list")
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), ApiConstants.CODE_401);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                            ApiConstants.INVALID_API_KEY);
    }

    @Test(description = "DELETE media with invalid media type")
    public void deleteItemWithInvalidMediaTypeTest() {
        File file = new File(".//src//test//resources//invalidItemBody.json");

        Response response = given()
                .urlEncodingEnabled(false)
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json; charset=UTF-8")
                .and()
                .body(file)
                .when()
                .delete("/list/7109519" + "/items")
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_500);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                            ApiConstants.INTERNAL_ERROR);
    }

    @Test(description = "GET request with version 3")
    public void sendGetRequestWithVersion3Test() {
        RestAssured.baseURI = "https://api.themoviedb.org/3";
        Response response = given()
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .and()
                .when()
                .get("list/7109537" + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), ApiConstants.CODE_404);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                ApiConstants.RESOURCE_NOT_FOUND);
    }

    @Test(description = "POST without Content-Type header")
    public void sendRequestWithEmptyHeadersTest() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "My favorite movie list");
        requestParams.put("iso_639_1", "en");

        Response response = given()
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .and()
                .body(requestParams.toJSONString())
                .when()
                .post("/list/7109519/items")
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), ApiConstants.CODE_404);
    }

}
