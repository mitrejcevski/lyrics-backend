package nl.jovmit.lyrics.app;

public class AppLauncher {

    public static void main(String... $EXPR) {
        String configuration = System.getenv("LYRICS_BACKEND_CONFIG_ENV");
        boolean isProduction = "file_based".equals(configuration);
        new LyricsApp().start(isProduction);
    }
}
