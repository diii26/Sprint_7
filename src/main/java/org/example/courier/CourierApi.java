package org.example.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.example.common.Constants.COURIER_BASE_URL;

public class CourierApi {

    @Step("Send POST request to " + COURIER_BASE_URL)
    public static Response sendPostRequestCourier(String login, String password, String firstName) {
        CreateCourier createCourier = new CreateCourier(login, password, firstName);
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .body(createCourier)
                .post(COURIER_BASE_URL);
    }

    @Step("Send POST request to " + COURIER_BASE_URL + "/login")
    public static Response sendPostRequestCourierLogin(String login, String password) {
        LoginCourier loginCourier = new LoginCourier(login, password);
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .body(loginCourier)
                .post(COURIER_BASE_URL + "/login");
    }

    @Step("Send DELETE request to " + COURIER_BASE_URL)
    public static Response sendDeleteRequestCourier(String id) {
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .when()
                .delete(COURIER_BASE_URL + "/{id}", id);
    }

}
