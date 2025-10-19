package org.example.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.common.CommonChecks;

import static org.hamcrest.Matchers.equalTo;

public class CourierChecks {

    @Step("Check that body's field ok is true")
    public static void checkThatBodyHasFieldOkAndItsValueIsTrue(Response response){
        response.then().body("ok", equalTo(true));
    }

    @Step("Check that body's field message contains correct value")
    public static void checkThatBodyHasFieldMessageAndItsValueIsCorrect(Response response, String expected) {
        response.then().body("message", equalTo(expected));
    }

    @Step("Check that body's field id contains not null value")
    public static void checkThatBodyHasFieldIdAndItsValueIsNotNull(Response response) {
        CommonChecks.checkThatBodyHasSomeFieldAndItsValueIsNotNull(response, "id");
    }

}
