package nl.jovmit.lyrics.app

import nl.jovmit.lyrics.api.LoginApi
import nl.jovmit.lyrics.api.SearchApi
import nl.jovmit.lyrics.api.UsersApi
import nl.jovmit.lyrics.domain.search.SearchService
import nl.jovmit.lyrics.domain.songs.InMemorySongRepository
import nl.jovmit.lyrics.domain.songs.SongService
import nl.jovmit.lyrics.domain.songs.SongsApi
import nl.jovmit.lyrics.domain.users.LoginService
import nl.jovmit.lyrics.domain.users.InMemoryUserRepository
import nl.jovmit.lyrics.domain.users.UserService
import nl.jovmit.lyrics.infrastructure.utils.IdGenerator
import spark.Spark.*

class Routes {

    private lateinit var loginApi: LoginApi
    private lateinit var usersApi: UsersApi
    private lateinit var songsApi: SongsApi
    private lateinit var searchApi: SearchApi

    fun create() {
        createApis()
        openLyricsRoutes()
    }

    private fun createApis() {
        val idGenerator = IdGenerator()

        val userRepository = InMemoryUserRepository()
        val userService = UserService(idGenerator, userRepository)
        val loginService = LoginService(userRepository)
        val songRepository = InMemorySongRepository()
        val songService = SongService(idGenerator, songRepository, userRepository)
        val searchService = SearchService(songRepository, userRepository)

        usersApi = UsersApi(userService)
        loginApi = LoginApi(loginService)
        songsApi = SongsApi(songService)
        searchApi = SearchApi(searchService)
    }

    private fun openLyricsRoutes() {
        get("status") { _, _ -> "Lyrics App: OK!" }
        post("users") { request, response -> usersApi.createUser(request, response) }
        post("login") { request, response -> loginApi.login(request, response) }
        post("users/:userId/songs") { request, response -> songsApi.createSong(request, response) }
        get("users/:userId/songs") { request, response -> songsApi.songsByUser(request, response) }
        get("users/:userId/songs/:songId") { request, response -> songsApi.songById(request, response) }
        put("users/:userId/songs/:songId") { request, response -> songsApi.editSong(request, response) }
        delete("users/:userId/songs/:songId") { request, response -> songsApi.deleteSong(request, response) }
        get("users/:userId/songs/search/:keyword") { request, response -> searchApi.searchSong(request, response) }
    }
}