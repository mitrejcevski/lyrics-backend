package nl.jovmit.lyrics.app

fun main() {
    val configuration = System.getenv("LYRICS_BACKEND_CONFIG_ENV")
    val isProduction = configuration == "file_based"
    LyricsApp().start(isProduction)
}
