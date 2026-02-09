package APItest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import org.testng.annotations.BeforeClass;
import java.util.HashMap;

public class GenerateAuthToken {

    protected String token;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        generateToken();
    }

    // Generate auth token
    public void generateToken() {
        HashMap<String, String> authPayload = new HashMap<>();
        authPayload.put("username", "admin");
        authPayload.put("password", "password123");

        token = given()
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
}