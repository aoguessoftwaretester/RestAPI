package APItest;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
//import io.restassured.response.ResponseBodyExtractionOptions;
//import io.restassured.specification.RequestSpecification;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
//import javax.swing.text.AbstractDocument.Content;

public class VerifyAuthentication {
	
	public final String BASE_URL = "https://restful-booker.herokuapp.com";
		
	@Test
	public void testCorrectAuth()
	{
		// Create request body
		HashMap<String, String> authBody = new HashMap<>();
		authBody.put("username", "admin");
		authBody.put("password", "password123");
		
		// Send POST Method
		Response response = 
			given()
				.baseUri(BASE_URL)
				.contentType(ContentType.JSON)
				.body(authBody)
			.when()
			    .post("/auth")
			.then()
			    .statusCode(200)
			    .contentType(ContentType.JSON)
			    .extract()
			    .response();
		
		// Assert that token exist
		String token = response.jsonPath().getString("token");
		Assert.assertNotNull(token, "Token should not be null");
	    Assert.assertFalse(token.isEmpty(), "Token should not be empty");
		//assert token != null && !token.isEmpty();
		System.out.println("Generated Token: " + token);
	}
	
	@Test
    public void testAuth_WrongPassword() {
        HashMap<String, String> authBody = new HashMap<>();
        authBody.put("username", "admin");
        authBody.put("password", "wrongpassword");

        given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .body(authBody)
        .when()
            .post("/auth")
        .then()
            .statusCode(200)               // The API returns 200 even for bad creds
            .body("reason", org.hamcrest.Matchers.equalTo("Bad credentials"));
    }

	@Test
    public void testAuth_WrongUsername() {
        HashMap<String, String> authBody = new HashMap<>();
        authBody.put("username", "wrongusername");
        authBody.put("password", "password123");

        given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .body(authBody)
        .when()
            .post("/auth")
        .then()
            .statusCode(200)               // The API returns 200 even for bad creds
            .body("reason", org.hamcrest.Matchers.equalTo("Bad credentials"));
    }

	@Test
    public void testAuth_BlankUser() {
        HashMap<String, String> authBody = new HashMap<>();
        authBody.put("username", "");
        authBody.put("password", "");

        given()
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON)
            .body(authBody)
        .when()
            .post("/auth")
        .then()
            .statusCode(200)               // Still returns 200
            .body("reason", org.hamcrest.Matchers.equalTo("Bad credentials"));
    }

}
