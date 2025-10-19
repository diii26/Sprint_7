import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.example.common.CommonChecks.checkThatStatusCodeIsCorrect;
import static org.example.common.Constants.BASE_URL;
import static org.example.order.OrderChecks.checkThatBodyHasFieldOrdersAndItsValueIsNotNull;
import static org.example.common.Constants.ORDERS_BASE_URL;
import static org.example.order.OrderApi.sendGetRequestOrders;

public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
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

}
