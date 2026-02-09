package APItest;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
//import io.restassured.response.ResponseBodyExtractionOptions;
//import io.restassured.specification.RequestSpecification;

import org.testng.annotations.Test;

import java.util.HashMap;
//import javax.swing.text.AbstractDocument.Content;

public class VerifyAuthentication {
	
	public final String BASE_URL = "https://restful-booker.herokuapp.com";
		
	@Test(priority = 1)
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
		assert token != null && !token.isEmpty();
		System.out.println("Generated Token: " + token);
	}
	
	@Test(priority = 2)
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


}
