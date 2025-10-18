import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.courier.CreateCourier;
import org.example.courier.DeleteCourierResponse;
import org.example.courier.LoginCourier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.example.courier.Constants.COURIER_BASE_URL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    public static String login = "qwertyasdfg7";
    public static String password = "asdfgqwerty";
    public static String firstName = "Asyav";

    @Before
    public void setUp() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru";
        sendPostRequestCourier(login, password, firstName);
    }

    @After
    public void tearDown() {
        try {
            Response loginResponse = sendPostRequestCourierLogin(login, password);
            sendDeleteRequestCourier("" + getId(loginResponse));
        } catch (UnrecognizedPropertyException ex) {
            //это нормально в случае если курьер не существует
        }
    }

    //курьер может авторизоваться
    //для авторизации нужно передать все обязательные поля
    //успешный запрос возвращает id
    @Test
    @DisplayName("Check status code of " + COURIER_BASE_URL)
    @Description("Basic test for " + COURIER_BASE_URL + " endpoint")
    public void loginCourier() {
        Response response = sendPostRequestCourierLogin(login, password);

        checkThatStatusCodeIsCorrect(response, 200);
        checkThatBodyHasFieldIdAndItsValueIsNotNull(response);
    }

    //система вернёт ошибку, если неправильно указать логин или пароль
    //если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
    @Test
    @DisplayName("Check status code of " + COURIER_BASE_URL)
    @Description("Basic test for " + COURIER_BASE_URL + " endpoint")
    public void loginCourierWithWrongLoginOrPassword() {
        Response response = sendPostRequestCourierLogin(password, password);
        Response response2 = sendPostRequestCourierLogin(login, login);

        checkThatStatusCodeIsCorrect(response, 404);
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response, "Учетная запись не найдена");
        checkThatStatusCodeIsCorrect(response2, 404);
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response2, "Учетная запись не найдена");
    }

    //если какого-то поля нет, запрос возвращает ошибку
    @Test
    @DisplayName("Check status code of " + COURIER_BASE_URL)
    @Description("Basic test for " + COURIER_BASE_URL + " endpoint")
    public void loginCourierWithEmptyLoginOrPassword() {
        Response response = sendPostRequestCourierLogin(null, password);
        Response response2 = sendPostRequestCourierLogin(login, null);

        checkThatStatusCodeIsCorrect(response, 400);
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response, "Недостаточно данных для входа");
        checkThatStatusCodeIsCorrect(response2, 400);//баг, возвращается 504 код вместо 400
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response2, "Недостаточно данных для входа");
    }

    @Step("Send POST request to " + COURIER_BASE_URL)
    private Response sendPostRequestCourier(String login, String password, String firstName) {
        CreateCourier createCourier = new CreateCourier(login, password, firstName);
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .body(createCourier)
                .post(COURIER_BASE_URL);
    }

    @Step("Send POST request to " + COURIER_BASE_URL + "/login")
    private Response sendPostRequestCourierLogin(String login, String password) {
        LoginCourier loginCourier = new LoginCourier(login, password);
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .body(loginCourier)
                .post(COURIER_BASE_URL + "/login");
    }

    @Step("Send DELETE request to " + COURIER_BASE_URL)
    private Response sendDeleteRequestCourier(String id) {
        return given()
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .when()
                .delete(COURIER_BASE_URL + "/{id}", id);
    }

    @Step("Check that statusCode is correct")
    private void checkThatStatusCodeIsCorrect(Response response, int expected){
        response.then().statusCode(expected);
    }

    @Step("Check that body's field message contains not null value")
    private void checkThatBodyHasFieldIdAndItsValueIsNotNull(Response response) {
        response.then().body("id", notNullValue());
    }

    @Step("Check that body's field message contains correct value")
    private void checkThatBodyHasFieldMessageAndItsValueIsCorrect(Response response, String expected) {
        response.then().body("message", equalTo(expected));
    }

    private int getId(Response response) throws UnrecognizedPropertyException {
        return response.body().as(DeleteCourierResponse.class).getId();
    }
}
