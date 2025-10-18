import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.example.courier.Constants.ORDERS_BASE_URL;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru";
    }

    //Проверь, что в тело ответа возвращается список заказов
    @Test
    @DisplayName("Check status code of " + ORDERS_BASE_URL)
    @Description("Basic test for " + ORDERS_BASE_URL + " endpoint")
    public void getOrders() {
        Response response = sendGetRequestOrders();

        checkThatStatusCodeIsCorrect(response, 200);
        checkThatBodyHasFieldOrdersAndItsValueIsNotNull(response);
    }

    @Step("Send GET request to " + ORDERS_BASE_URL)
    private Response sendGetRequestOrders() {
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .get(ORDERS_BASE_URL);
    }

    @Step("Check that statusCode is correct")
    private void checkThatStatusCodeIsCorrect(Response response, int expected){
        response.then().statusCode(expected);
    }

    @Step("Check that body's field orders contains values")
    private void checkThatBodyHasFieldOrdersAndItsValueIsNotNull(Response response) {
        response.then().body("orders", notNullValue());
    }
}
