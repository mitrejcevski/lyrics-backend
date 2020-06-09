package integration;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import static integration.ApiTestSuite.BASE_URL;
import static integration.ApiTestSuite.UUID_PATTERN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

public class IT_LoginApi {

    @Test
    public void login_user() {
        given()
                .body(withJsonContaining("username", "password", "about"))
                .post(BASE_URL + "/users");
        given()
                .body(withJsonContaining("username", "password"))
        .when()
                .post(BASE_URL + "/login")
        .then()
                .statusCode(200)
                .contentType("application/json")
                .body("id", matchesPattern(UUID_PATTERN))
                .body("username", is("username"))
                .body("about", is("about"));
    }

    private String withJsonContaining(String username, String password, String about) {
        return new JsonObject()
                .add("username", username)
                .add("password", password)
                .add("about", about)
                .toString();
    }

    private String withJsonContaining(String username, String password) {
        return new JsonObject()
                .add("username", username)
                .add("password", password)
                .toString();
    }
}
