package integration;

import com.eclipsesource.json.JsonObject;
import integration.data.IT_SongData;
import org.junit.Test;

import java.util.UUID;

import static integration.ApiTestSuite.BASE_URL;
import static integration.ApiTestSuite.UUID_PATTERN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class IT_UsersApi {

    private static final String USER_ID = UUID.randomUUID().toString();
    private static final String GUEST_ID = UUID.randomUUID().toString();
    private static final IT_SongData BEAUTIFUL_BY_AKON = new IT_SongData("Beautiful", "Akon", "Beautiful song lyrics...");
    private static final IT_SongData YEAH_BY_USHER = new IT_SongData("Yeah yeah", "Usher", "Yeah yeah song lyrics...");
    private static final IT_SongData ALWAYS_BY_BON_JOVI = new IT_SongData("Always", "Bon Jovi", "Always song lyrics...");

    @Test
    public void create_a_song() {
        given()
                .body(withJsonContainingSongData(BEAUTIFUL_BY_AKON))
        .when()
                .post(BASE_URL + "/users/" + USER_ID + "/songs")
        .then()
                .statusCode(201)
                .contentType("application/json")
                .body("userId", is(USER_ID))
                .body("songId", matchesPattern(UUID_PATTERN))
                .body("songTitle", is(BEAUTIFUL_BY_AKON.title()))
                .body("songPerformer", is(BEAUTIFUL_BY_AKON.performer()))
                .body("songLyrics", is(BEAUTIFUL_BY_AKON.lyrics()));
    }

    @Test
    public void return_songs_for_user() {
        given()
                .body(withJsonContainingSongData(BEAUTIFUL_BY_AKON))
                .post(BASE_URL + "/users/" + USER_ID + "/songs");
        given()
                .body(withJsonContainingSongData(YEAH_BY_USHER))
                .post(BASE_URL + "/users/" + USER_ID + "/songs");
        given()
                .body(withJsonContainingSongData(ALWAYS_BY_BON_JOVI))
                .post(BASE_URL + "/users/" + GUEST_ID + "/songs");
        given()
                .get(BASE_URL + "/users/" + USER_ID + "/songs")
        .then()
                .statusCode(200)
                .contentType("application/json")
                .body("songs", hasSize(2))
                .body("userId", everyItem(is(USER_ID)))
                .body("songId", everyItem(matchesPattern(UUID_PATTERN)))
                .body("songTitle", containsInAnyOrder(BEAUTIFUL_BY_AKON.title(), YEAH_BY_USHER.title()))
                .body("songPerformer", containsInAnyOrder(BEAUTIFUL_BY_AKON.performer(), YEAH_BY_USHER.performer()))
                .body("songLyrics", containsInAnyOrder(BEAUTIFUL_BY_AKON.lyrics(), YEAH_BY_USHER.lyrics()));
    }

    private String withJsonContainingSongData(IT_SongData songData) {
        return new JsonObject()
                .add("songTitle", songData.title())
                .add("songPerformer", songData.performer())
                .add("songLyrics", songData.lyrics())
                .toString();
    }
}
