package com.app.test;

import com.app.util.ApiConstants;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static io.restassured.RestAssured.given;

/**
 * @author rsinghnagi
 * This class contains positive tests for Movie DB
 */
public class MovieListPositiveTests extends TestBase {

    private String createdID;

    @Test(description = "To create the new List")
    public void createListTest() {

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "My favorite movie list");
        requestParams.put("iso_639_1", "en");

        Response response = given()
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
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

        createdID = response.jsonPath().getString("id");
        System.out.println("List is created: " + createdID);
        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_201);
        Assert.assertEquals(response.jsonPath().getString("status_code"),
                            ApiConstants.STATUS_CODE_1);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                            ApiConstants.THE_ITEM_WAS_CREATED_SUCCESSFULLY);
        Assert.assertEquals(response.jsonPath().getString("success"), ApiConstants.TRUE);
    }

    @Test(description = "To fetch the List created in the POST request", dependsOnMethods = {"createListTest"})
    public void getCreatedListTest() {
        io.restassured.response.Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/list/" + createdID + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_200);
        Assert.assertEquals(response.jsonPath().getString("name"), ApiConstants.PARAM_NAME);
        Assert.assertEquals(response.jsonPath().getString("id"), createdID);
        Assert.assertEquals(response.jsonPath().getString("iso_639_1"), ApiConstants.ISO_639_1);
        Assert.assertEquals(response.jsonPath().getString("description"), "");
    }

    @Test(description = "Update the List created in POST request", dependsOnMethods = {"getCreatedListTest"})
    public void updateListTest() {

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "My updated favorite movie list");

        Response response = given()
                .urlEncodingEnabled(false)
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json; charset=UTF-8")
                .and()
                .body(requestParams.toJSONString())
                .when()
                .put("/list/" + createdID + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_201);
        Assert.assertEquals(response.jsonPath().getString("status_code"),
                                ApiConstants.STATUS_CODE_12);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                            ApiConstants.THE_ITEM_WAS_UPDATED_SUCCESSFULLY);
        Assert.assertEquals(response.jsonPath().getString("success"), ApiConstants.TRUE);
    }

    // @Test
    public void updateListViaHttpClientLibraryTest() throws IOException {

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "My updated favorite movie list");
        requestParams.put("description",
                "I would like to add the Horror, Animated, Suspense and Action Genres in this list");

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create("{\n  \"name\": \"Updated the list name\",\n  \"description\": \"This is it.\"\n}", mediaType);
        Request request = new Request.Builder()
                .url(ApiConstants.BASE_URL + "/list/" + createdID + ApiConstants.API_KEY)
                .method("PUT", body)
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjE2MzI1NDc1NzcsInN1YiI6IjYxNGEwNGQxOWE5ZTIwMDA0MzNhNjNmOCIsImp0aSI6IjM1NDkyNTgiLCJzY29wZXMiOlsiYXBpX3JlYWQiLCJhcGlfd3JpdGUiXSwidmVyc2lvbiI6MSwiYXVkIjoiZDYyNWRiMmRlNTQzNDExYTcwMGZjNmI2MDBkNzY0MTEifQ.OQBqtJIh7EjgWRjH68SZnB2X6lc0QoKFK85SAz_qtec")
                .addHeader("Content-Type", "application/json")
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 201);
    }

    @Test(description = "Get the List updated from the PUT request", dependsOnMethods = {"updateListTest"})
    public void getUpdatedListTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/list/" + createdID + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_200);
        Assert.assertEquals(response.jsonPath().getString("name"),
                            ApiConstants.UPDATED_LIST_NAME);
        Assert.assertEquals(response.jsonPath().getString("id"), createdID);
        Assert.assertEquals(response.jsonPath().getString("iso_639_1"), ApiConstants.ISO_639_1);
    }

    @Test(description = "To add the items to the existing list", dependsOnMethods = {"getUpdatedListTest"})
    public void addItemTest() throws UnsupportedEncodingException, FileNotFoundException {
        File file = new File(".//src//test//resources//addItemBody.json");

        Response response = given()
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .and()
                .body(file)
                .when()
                .post("/list/" + createdID + "/items")
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_200);
        Assert.assertEquals(response.jsonPath().getString("results[0].media_id"),
                            ApiConstants.MEDIA_TYPE_550);
        Assert.assertEquals(response.jsonPath().getString("results[1].media_id"),
                            ApiConstants.MEDIA_TYPE_244786);
        Assert.assertEquals(response.jsonPath().getString("results[2].media_id"),
                            ApiConstants.MEDIA_TYPE_1396);
    }

    @Test(description = "Get items after the addition", dependsOnMethods = {"addItemTest"})
    public void getAddedItemFromTheListTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/list/" + createdID + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_200);
    }

    @Test(description = "To update the item", dependsOnMethods = {"getAddedItemFromTheListTest"})
    public void updateItemTest() {
        File file = new File(".//src//test//resources//updateItemBody.json");

        Response response = given()
                .urlEncodingEnabled(false)
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json; charset=UTF-8")
                .and()
                .body(file)
                .when()
                .put("/list/" + createdID + "/items" + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_200);
        Assert.assertEquals(response.jsonPath().getString("status_code"),
                            ApiConstants.STATUS_CODE_1);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                            ApiConstants.SUCCESS);
    }

    @Test(description = "To get the item and verify in the list", dependsOnMethods = {"updateItemTest"})
    public void getNewlyAddedCommentFromTheListTest() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/list/" + createdID + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_200);
    }

    @Test(description = "To delete the item from the list", dependsOnMethods = {"getNewlyAddedCommentFromTheListTest"})
    public void deleteItemTest() {
        File file = new File(".//src//test//resources//deleteItemBody.json");

        Response response = given()
                .urlEncodingEnabled(false)
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json; charset=UTF-8")
                .and()
                .body(file)
                .when()
                .delete("/list/" + createdID + "/items" + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_200);
        Assert.assertEquals(response.jsonPath().getString("status_code"),
                ApiConstants.STATUS_CODE_1);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                            ApiConstants.SUCCESS);
        Assert.assertEquals(response.jsonPath().getString("results[0].media_id"), "550");
        Assert.assertEquals(response.jsonPath().getString("results[0].media_type"), "movie");
        Assert.assertEquals(response.jsonPath().getString("results[0].success"), "true");
    }

    @Test (description = "To clear the item from the list", dependsOnMethods = {"deleteItemTest"})
    public void clearListTest() {
        Response response = given()
                .urlEncodingEnabled(false)
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json; charset=UTF-8")
                .and()
                .when()
                .get("/list/" + createdID + "/clear" + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_200);
        Assert.assertEquals(response.jsonPath().getString("id"), createdID);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                            ApiConstants.SUCCESS);
        Assert.assertEquals(response.jsonPath().getString("success"), ApiConstants.TRUE);
    }

    @Test(description = "To delete the list", dependsOnMethods = {"clearListTest"})
    public void deleteListTest() {
        Response response = given()
                .urlEncodingEnabled(false)
                .header("Authorization", "Bearer " + ApiConstants.ACCESS_BEARER_TOKEN)
                .header("Content-Type", "application/json; charset=UTF-8")
                .and()
                .when()
                .delete("/list/" + createdID + ApiConstants.API_KEY)
                .then()
                .log()
                .body()
                .extract()
                .response();

        System.out.println("List is deleted: " + createdID);
        Assert.assertEquals(response.statusCode(), ApiConstants.CODE_200);
        Assert.assertEquals(response.jsonPath().getString("status_code"),
                            ApiConstants.STATUS_CODE_13);
        Assert.assertEquals(response.jsonPath().getString("status_message"),
                            ApiConstants.THE_ITEM_WAS_DELETED_SUCCESSFULLY);
        Assert.assertEquals(response.jsonPath().getString("success"), ApiConstants.TRUE);
    }
}
