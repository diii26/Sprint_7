package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.common.CommonChecks;

import static org.hamcrest.Matchers.notNullValue;

public class OrderChecks {

    @Step("Check that body's field orders contains values")
    public static void checkThatBodyHasFieldOrdersAndItsValueIsNotNull(Response response) {
        response.then().body("orders", notNullValue());
    }

    @Step("Check that body's field message contains not null value")
    public static void checkThatBodyHasFieldTrackAndItsValueIsNotNull(Response response) {
        CommonChecks.checkThatBodyHasSomeFieldAndItsValueIsNotNull(response, "track");
    }

}
