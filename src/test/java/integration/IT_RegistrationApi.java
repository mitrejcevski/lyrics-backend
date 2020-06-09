package integration;

import com.eclipsesource.json.JsonObject;
import nl.jovmit.lyrics.app.LyricsApp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class IT_RegistrationApi {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String UUID_PATTERN = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";

    private static LyricsApp app;

    @BeforeAll
    static void before_all() {
        app = new LyricsApp();
        app.start();
        app.awaitInitialization();
    }

    @AfterAll
    static void after_all() {
        app.stop();
    }

    @Test
    public void register_a_new_user() {
        given()
                .body(withJsonContaining("Lucy", "aslkda12s", "About Lucy"))
        .when()
                .post(BASE_URL+"/users")
        .then()
                .statusCode(201)
                .contentType("application/json")
                .body("id", matchesPattern(UUID_PATTERN))
                .body("username", is("Lucy"))
                .body("about", is("About Lucy"));
    }

    private String withJsonContaining(String username, String password, String about) {
        return new JsonObject()
                .add("username", username)
                .add("password", password)
                .add("about", about)
                .toString();
    }
}
