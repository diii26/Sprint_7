import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.order.PostOrdersResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.example.common.CommonChecks.checkThatStatusCodeIsCorrect;
import static org.example.common.Constants.BASE_URL;
import static org.example.common.Constants.ORDERS_BASE_URL;
import static org.example.order.OrderApi.sendPostRequestOrders;
import static org.example.order.OrderApi.sendPutRequestOrders;
import static org.example.order.OrderChecks.checkThatBodyHasFieldTrackAndItsValueIsNotNull;

//Чтобы протестировать создание заказа, нужно использовать параметризацию.
@RunWith(Parameterized.class)
public class OrderCreationTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    public OrderCreationTest(String firstName, String lastName, String address, int metroStation, String phone,
                             int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    // Тестовые данные
    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][] {
                { "vasya",  "pupkin",   "pushkina, 5", 1, "+7 800 355 35 35", 1, "2020-06-06", "comment 1", new String[]{}},
                { "vasya1", "pupkin1",  "pushkina, 6", 2, "+7 800 355 35 36", 2, "2020-06-07", "comment 2", new String[]{"BLACK"}},
                { "vasya2", "pupkin2",  "pushkina, 7", 3, "+7 800 355 35 37", 3, "2020-06-08", "comment 3", new String[]{"GREY"}},
                { "vasya3", "pupki3n",  "pushkina, 8", 4, "+7 800 355 35 38", 4, "2020-06-09", "comment 4", new String[]{"BLACK", "GREY"}}
        };
    }

    //можно указать один из цветов — BLACK или GREY
    //можно указать оба цвета
    //можно совсем не указывать цвет
    //тело ответа содержит track
    @Test
    @DisplayName("Check status code of " + ORDERS_BASE_URL)
    @Description("Basic test for " + ORDERS_BASE_URL + " endpoint")
    public void createOrder() {
        Response response = sendPostRequestOrders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        checkThatStatusCodeIsCorrect(response, 201);
        checkThatBodyHasFieldTrackAndItsValueIsNotNull(response);

        sendPutRequestOrders(getTrack(response));
    }

    public static int getTrack(Response response) {
        return response.body().as(PostOrdersResponse.class).getTrack();
    }
}
