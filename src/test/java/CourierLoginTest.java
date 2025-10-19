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
import static org.example.common.Constants.BASE_URL;
import static org.example.courier.CourierChecks.*;
import static org.example.common.Constants.COURIER_BASE_URL;
import static org.example.courier.CourierApi.*;

public class CourierLoginTest {

    public static String login = "qwertyasdfg7";
    public static String password = "asdfgqwerty";
    public static String firstName = "Asyav";

    @Before
    public void setUp() {
        RestAssured.baseURI= BASE_URL;
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
    public void loginCourierWithEmptyLogin() {
        Response response = sendPostRequestCourierLogin(null, password);

        checkThatStatusCodeIsCorrect(response, 400);
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Check status code of " + COURIER_BASE_URL)
    @Description("Basic test for " + COURIER_BASE_URL + " endpoint")
    public void loginCourierWithEmptyPassword() {
        Response response2 = sendPostRequestCourierLogin(login, null);

        checkThatStatusCodeIsCorrect(response2, 400);//баг, возвращается 504 код вместо 400
        checkThatBodyHasFieldMessageAndItsValueIsCorrect(response2, "Недостаточно данных для входа");
    }

    public static int getId(Response response) throws UnrecognizedPropertyException {
        return response.body().as(DeleteCourierResponse.class).getId();
    }

}
