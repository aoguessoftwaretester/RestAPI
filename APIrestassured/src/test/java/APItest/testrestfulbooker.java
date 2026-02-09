package APItest;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import org.testng.annotations.*;
import io.restassured.response.Response;
import java.util.HashMap;

public class testrestfulbooker {
	
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

 // ---------- CREATE BOOKING ----------
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
    }
    
	// ---------- GET ALL BOOKINGS ----------
    @Test
    public void getAllBookings() {
        given()
        .when()
            .get("/booking")
        .then()
            .statusCode(200)
            .log().body();  // Prints the response body
    }

    // ---------- GET BOOKING BY ID ----------
    @Test
    public void getBookingById() {
        given()
            .pathParam("id", bookingId)
        .when()
            .get("/booking/{id}")
        .then()
            .statusCode(200)
            .log().body();  // Prints the response body
    }
    
 // ---------- UPDATE BOOKING ----------
    @Test
    public void updateBooking() {
        HashMap<String, Object> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2025-02-01");
        bookingDates.put("checkout", "2025-02-15");

        HashMap<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("firstname", "Jane");
        updatePayload.put("lastname", "Smith");
        updatePayload.put("totalprice", 300);
        updatePayload.put("depositpaid", false);
        updatePayload.put("bookingdates", bookingDates);
        updatePayload.put("additionalneeds", "Lunch");

        given()
            .contentType(ContentType.JSON)
            .cookie("token", token)
            .pathParam("id", bookingId)
            .body(updatePayload)
        .when()
            .put("/booking/{id}")
        .then()
            .statusCode(200)
            .log().body();
    }
    
    // ---------- DELETE BOOKING ----------
    @Test(dependsOnMethods = "updateBooking")
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
