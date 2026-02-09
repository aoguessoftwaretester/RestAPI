package APItest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class VerifyCreateBookingResponsePayload extends GenerateAuthToken {

    private int bookingId;

    @Test(priority = 1)
    public void createBookingAndValidateResponse() {
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

        Response response =
        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/booking");

        response.then().statusCode(200).log().body();
        bookingId = response.jsonPath().getInt("bookingid");

        Map<String, Object> responseBody = response.jsonPath().getMap("booking");

        assertThat(responseBody.get("firstname"), equalTo(payload.get("firstname")));
        assertThat(responseBody.get("lastname"), equalTo(payload.get("lastname")));
        assertThat(responseBody.get("totalprice"), equalTo(payload.get("totalprice")));
        assertThat(responseBody.get("depositpaid"), equalTo(payload.get("depositpaid")));
        assertThat(((Map) responseBody.get("bookingdates")).get("checkin"), equalTo(bookingDates.get("checkin")));
        assertThat(((Map) responseBody.get("bookingdates")).get("checkout"), equalTo(bookingDates.get("checkout")));
        assertThat(responseBody.get("additionalneeds"), equalTo(payload.get("additionalneeds")));
    }

    @Test(priority = 2)
    public void deleteBooking() {
        given()
            .cookie("token", token)
            .pathParam("id", bookingId)
        .when()
            .delete("/booking/{id}")
        .then()
            .statusCode(201)
            .log().body();
    }
}
