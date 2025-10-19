package org.example.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.example.common.Constants.ORDERS_BASE_URL;

public class OrderApi {
    @Step("Send POST request to " + ORDERS_BASE_URL)
    public static Response sendPostRequestOrders(String firstName, String lastName, String address, int metroStation,
                                           String phone, int rentTime, String deliveryDate, String comment,
                                           String[] color) {
        CreateOrder createOrder = new CreateOrder(firstName, lastName, address, metroStation, phone, rentTime,
                deliveryDate, comment, color);
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .body(createOrder)
                .post(ORDERS_BASE_URL);
    }

    @Step("Send GET request to " + ORDERS_BASE_URL)
    public static Response sendGetRequestOrders() {
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .get(ORDERS_BASE_URL);
    }

    @Step("Send GET request to " + ORDERS_BASE_URL)
    public static Response sendPutRequestOrders(int track) {
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest(track);
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .body(cancelOrderRequest)
                .put(ORDERS_BASE_URL + "/cancel");
    }

}
