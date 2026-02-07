package APItest;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import org.testng.annotations.*;

import java.util.HashMap;

public class testrestfulbooker {

    @BeforeClass
    public void setup() {
        baseURI = "https://restful-booker.herokuapp.com";
    }

    @Test
    public void createBooking() {
        HashMap<String, Object> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2025-01-01");
        bookingDates.put("checkout", "2025-01-10");

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("firstname", "John");
        payload.put("lastname", "Doe");
        payload.put("totalprice", 150);
        payload.put("depositpaid", true);
        payload.put("bookingdates", bookingDates);
        payload.put("additionalneeds", "Breakfast");

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/booking")
        .then()
            .statusCode(200);
    }
    
    @Test
    public void getAllBookings() {
        given()
        .when()
            .get("/booking")
        .then()
            .statusCode(200);
    }

    @Test
    public void getBookingById() {
        given()
            .pathParam("id", 1)
        .when()
            .get("/booking/{id}")
        .then()
            .statusCode(200);
    }
    
}
