package nl.jovmit.lyrics.app

import nl.jovmit.lyrics.api.LoginApi
import nl.jovmit.lyrics.api.UsersApi
import nl.jovmit.lyrics.domain.songs.SongRepository
import nl.jovmit.lyrics.domain.songs.SongService
import nl.jovmit.lyrics.domain.songs.SongsApi
import nl.jovmit.lyrics.domain.users.LoginService
import nl.jovmit.lyrics.domain.users.UserRepository
import nl.jovmit.lyrics.domain.users.UserService
import nl.jovmit.lyrics.infrastructure.utils.IdGenerator
import spark.Spark.get
import spark.Spark.post

class Routes {

    private lateinit var loginApi: LoginApi
    private lateinit var usersApi: UsersApi
    private lateinit var songsApi: SongsApi

    fun create() {
        createApis()
        openLyricsRoutes()
    }

    private fun createApis() {
        val idGenerator = IdGenerator()

        val userRepository = UserRepository()
        val userService = UserService(idGenerator, userRepository)
        val loginService = LoginService(userRepository)
        val songRepository = SongRepository()
        val songService = SongService(idGenerator, songRepository, userRepository)

        usersApi = UsersApi(userService)
        loginApi = LoginApi(loginService)
        songsApi = SongsApi(songService)
    }

    private fun openLyricsRoutes() {
        get("status") { _, _ -> "Lyrics App: OK!" }
        post("users") { request, response -> usersApi.createUser(request, response) }
        post("login") { request, response -> loginApi.login(request, response) }
        post("users/:userId/songs") { request, response -> songsApi.createSong(request, response) }
        get("users/:userId/songs") { request, response -> songsApi.songsByUser(request, response) }
    }
}