package com.app.test;

import com.app.util.ApiConstants;
import com.app.util.ApiUtil;
import io.restassured.RestAssured;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

/**
 * @author rsinghnagi
 * This class contains some common methods to be used throughout test classes
 */
public class TestBase extends ApiUtil {

    @BeforeTest
    public static void setup() {
        RestAssured.baseURI = ApiConstants.BASE_URL;
    }

    @BeforeMethod
    public void runBeforeMethods() {
        System.out.println("==================Test Starts==================");
    }

    @AfterMethod
    public void runAfterMethods() {
        System.out.println("==================Test Ends==================");
    }

}
