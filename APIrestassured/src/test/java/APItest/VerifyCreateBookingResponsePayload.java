package APItest;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
//import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class VerifyCreateBookingResponsePayload {
	
	String token;
	int bookingId;

    @BeforeClass
    public void setup() {
        baseURI = "https://restful-booker.herokuapp.com";
        generateToken();
    }

 // ---------- AUTH TOKEN ----------
    public void generateToken() {
        HashMap<String, String> authPayload = new HashMap<>();
        authPayload.put("username", "admin");
        authPayload.put("password", "password123");

        token =
            given()
                .contentType(ContentType.JSON)
                .body(authPayload)
            .when()
                .post("/auth")
            .then()
                .statusCode(200)
                .extract()
                .path("token");

        System.out.println("Generated Token: " + token);
    }
	
	@Test(priority = 1)
    public void createBookingAndValidateResponse() {

        // Booking details payload
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
        response.then()
            .statusCode(200)
            .log().body();  // Prints the response body
        
        // Capture bookingId
        bookingId = response.jsonPath().getInt("bookingid");
        System.out.println("Created Booking ID: " + bookingId);
        
        // Extract response JSON
        Map<String, Object> responseBody = response.jsonPath().getMap("booking");

        // Compare each field
        assertThat(responseBody.get("firstname"), equalTo(payload.get("firstname")));
        assertThat(responseBody.get("lastname"), equalTo(payload.get("lastname")));
        assertThat(responseBody.get("totalprice"), equalTo(payload.get("totalprice")));
        assertThat(responseBody.get("depositpaid"), equalTo(payload.get("depositpaid")));
        assertThat(((Map) responseBody.get("bookingdates")).get("checkin"),
                equalTo(bookingDates.get("checkin")));
        assertThat(((Map) responseBody.get("bookingdates")).get("checkout"),
                equalTo(bookingDates.get("checkout")));
        assertThat(responseBody.get("additionalneeds"), equalTo(payload.get("additionalneeds")));
        
     // Optional: print the booking ID
        System.out.println("Booking created successfully with ID: " + response.jsonPath().getInt("bookingid"));
    }
	   // ---------- DELETE BOOKING ----------
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
