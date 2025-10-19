package org.example.common;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.notNullValue;

public class CommonChecks {
    @Step("Check that statusCode is correct")
    public static void checkThatStatusCodeIsCorrect(Response response, int expected){
        response.then().statusCode(expected);
    }

    public static void checkThatBodyHasSomeFieldAndItsValueIsNotNull(Response response, String fieldName) {
        response.then().body(fieldName, notNullValue());
    }
}
