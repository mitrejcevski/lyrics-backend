package nl.jovmit.lyrics.app

import spark.Spark.get

class Routes {

    fun create() {
        createApis()
        openLyricsRoutes()
    }

    private fun createApis() {

    }

    private fun openLyricsRoutes() {
        get("status") { _, _ -> "Lyrics App: OK!" }
    }
}