package nl.jovmit.lyrics.app

import nl.jovmit.lyrics.api.LoginApi
import nl.jovmit.lyrics.api.UsersApi
import nl.jovmit.lyrics.domain.users.LoginService
import nl.jovmit.lyrics.domain.users.UserRepository
import nl.jovmit.lyrics.domain.users.UserService
import nl.jovmit.lyrics.infrastructure.utils.IdGenerator
import spark.Spark.get
import spark.Spark.post

class Routes {

    private lateinit var loginApi: LoginApi
    private lateinit var usersApi: UsersApi

    fun create() {
        createApis()
        openLyricsRoutes()
    }

    private fun createApis() {
        val idGenerator = IdGenerator()

        val userRepository = UserRepository()
        val userService = UserService(idGenerator, userRepository)
        val loginService = LoginService(userRepository)

        usersApi = UsersApi(userService)
        loginApi = LoginApi(loginService)
    }

    private fun openLyricsRoutes() {
        get("status") { _, _ -> "Lyrics App: OK!" }
        post("users") { request, response -> usersApi.createUser(request, response) }
        post("login") { request, response -> loginApi.login(request, response) }
    }
}