import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.courier.DeleteCourierResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.common.CommonChecks.checkThatStatusCodeIsCorrect;
import static org.example.courier.CourierChecks.*;
import static org.example.common.Constants.BASE_URL;
import static org.example.common.Constants.COURIER_BASE_URL;
import static org.example.courier.CourierApi.*;

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
    public void createCourierWithoutLogin() {
        Response response = sendPostRequestCourier(null, password, firstName);

        checkThatStatusCodeIsCorrect(response, 400);
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Check status code of " + COURIER_BASE_URL)
    @Description("Basic test for " + COURIER_BASE_URL + " endpoint")
    public void createCourierWithoutPassword() {
        Response response2 = sendPostRequestCourier(login, null, firstName);

        checkThatStatusCodeIsCorrect(response2, 400);
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response2, "Недостаточно данных для создания учетной записи");
    }

    public static int getId(Response response) throws UnrecognizedPropertyException {
        return response.body().as(DeleteCourierResponse.class).getId();
    }

}
