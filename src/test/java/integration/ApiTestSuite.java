package integration;

import nl.jovmit.lyrics.app.LyricsApp;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        IT_RegistrationApi.class,
        IT_LoginApi.class,
        IT_UsersApi.class
})
public class ApiTestSuite {

    static final int PORT = 4321;
    static final String BASE_URL = "http://localhost:" + PORT;
    static final String UUID_PATTERN = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";

    private static LyricsApp app;

    @BeforeClass
    public static void setUp() {
        app = new LyricsApp();
        app.start(PORT, false);
        app.awaitInitialization();
    }

    @AfterClass
    public static void tearDown() {
        app.stop();
    }
}
