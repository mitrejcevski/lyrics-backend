package nl.jovmit.lyrics.app;

public class AppLauncher {

    public static void main(String... $EXPR) {
        String configuration = System.getenv("LYRICS_BACKEND_CONFIG_ENV");
        String port = System.getenv("PORT");
        int portValue = (port != null) ? Integer.parseInt(port) : 4321;
        boolean isProduction = "file_based".equals(configuration);
        new LyricsApp().start(portValue, isProduction);
    }
}
