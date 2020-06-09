package integration;

import com.eclipsesource.json.JsonObject;
import org.junit.Test;

import java.util.UUID;

import static integration.ApiTestSuite.BASE_URL;
import static integration.ApiTestSuite.UUID_PATTERN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

public class IT_UsersApi {

    private static final String USER_ID = UUID.randomUUID().toString();

    @Test
    public void create_a_song() {
        given()
                .body(withJsonContaining("Title", "Performer", "Lyrics"))
        .when()
                .post(BASE_URL + "/users/" + USER_ID + "/songs")
        .then()
                .statusCode(201)
                .contentType("application/json")
                .body("userId", is(USER_ID))
                .body("songId", matchesPattern(UUID_PATTERN))
                .body("songTitle", is("Title"))
                .body("songPerformer", is("Performer"))
                .body("songLyrics", is("Lyrics"));
    }

    private String withJsonContaining(String title, String performer, String lyrics) {
        return new JsonObject()
                .add("songTitle", title)
                .add("songPerformer", performer)
                .add("songLyrics", lyrics)
                .toString();
    }
}
