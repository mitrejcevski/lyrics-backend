package integration;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import static integration.ApiTestSuite.BASE_URL;
import static integration.ApiTestSuite.UUID_PATTERN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

public class IT_RegistrationApi {

    @Test
    public void register_a_new_user() {
        given()
                .body(withJsonContaining("Tom", "aslkda12s", "About Tom"))
        .when()
                .post(BASE_URL + "/users")
        .then()
                .statusCode(201)
                .contentType("application/json")
                .body("id", matchesPattern(UUID_PATTERN))
                .body("username", is("Tom"))
                .body("about", is("About Tom"));
    }

    private String withJsonContaining(String username, String password, String about) {
        return new JsonObject()
                .add("username", username)
                .add("password", password)
                .add("about", about)
                .toString();
    }
}
