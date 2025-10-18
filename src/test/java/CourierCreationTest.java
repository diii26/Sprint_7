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
import static org.example.courier.Constants.BASE_URL;
import static org.example.courier.Constants.COURIER_BASE_URL;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreationTest {

    public static String login = "qwertyasdfg7";
    public static String password = "asdfgqwerty";
    public static String firstName = "Asyav";

    @Before
    public void setUp() {
        RestAssured.baseURI= BASE_URL;
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

    //курьера можно создать
    //запрос возвращает правильный код ответа
    //успешный запрос возвращает ok: true
    @Test
    @DisplayName("Check status code of " + COURIER_BASE_URL)
    @Description("Basic test for " + COURIER_BASE_URL + " endpoint")
    public void createCourier() {
        Response response = sendPostRequestCourier(login, password, firstName);

        checkThatStatusCodeIsCorrect(response, 201);
        checkThatBodyHasFieldOkAndItsValueIsTrue(response);
    }

    //нельзя создать двух одинаковых курьеров
    //если создать пользователя с логином, который уже есть, возвращается ошибка
    @Test
    @DisplayName("Check status code of " + COURIER_BASE_URL)
    @Description("Basic test for " + COURIER_BASE_URL + " endpoint")
    public void createTwoIdenticalCouriers() {
        sendPostRequestCourier(login, password, firstName);
        Response response = sendPostRequestCourier(login, password, firstName);

        checkThatStatusCodeIsCorrect(response, 409);
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response, "Этот логин уже используется");//баг, сообщение не соответствует ожидаемому
    }

    //чтобы создать курьера, нужно передать в ручку все обязательные поля
    //если одного из полей нет, запрос возвращает ошибку
    @Test
    @DisplayName("Check status code of " + COURIER_BASE_URL)
    @Description("Basic test for " + COURIER_BASE_URL + " endpoint")
    public void createCourierWithoutPasswordOrLogin() {
        Response response = sendPostRequestCourier(null, password, firstName);
        Response response2 = sendPostRequestCourier(login, null, firstName);

        checkThatStatusCodeIsCorrect(response, 400);
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response, "Недостаточно данных для создания учетной записи");
        checkThatStatusCodeIsCorrect(response2, 400);
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response2, "Недостаточно данных для создания учетной записи");
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
                .header(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
                .when()
                .delete(COURIER_BASE_URL + "/{id}", id);
    }

    @Step("Check that body's field ok is true")
    private void checkThatBodyHasFieldOkAndItsValueIsTrue(Response response){
        response.then().body("ok", equalTo(true));
    }

    @Step("Check that body's field message contains correct value")
    private void checkThatBodyHasFieldMessageAndItsValueIsCorrect(Response response, String expected) {
        response.then().body("message", equalTo(expected));
    }

    @Step("Check that statusCode is correct")
    private void checkThatStatusCodeIsCorrect(Response response, int expected){
        response.then().statusCode(expected);
    }

    private int getId(Response response) throws UnrecognizedPropertyException {
        return response.body().as(DeleteCourierResponse.class).getId();
    }

}
