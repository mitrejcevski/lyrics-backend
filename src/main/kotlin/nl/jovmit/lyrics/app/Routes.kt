package nl.jovmit.lyrics.app

import nl.jovmit.lyrics.api.UsersApi
import nl.jovmit.lyrics.domain.users.UserRepository
import nl.jovmit.lyrics.domain.users.UserService
import nl.jovmit.lyrics.infrastructure.utils.IdGenerator
import spark.Spark.get
import spark.Spark.post

class Routes {

    private lateinit var usersApi: UsersApi

    fun create() {
        createApis()
        openLyricsRoutes()
    }

    private fun createApis() {
        val idGenerator = IdGenerator()

        val userRepository = UserRepository()
        val userService = UserService(idGenerator, userRepository)

        usersApi = UsersApi(userService)
    }

    private fun openLyricsRoutes() {
        get("status") { _, _ -> "Lyrics App: OK!" }
        post("users") { request, response -> usersApi.createUser(request, response) }
    }
}