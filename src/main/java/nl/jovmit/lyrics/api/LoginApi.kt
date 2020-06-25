package nl.jovmit.lyrics.api

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.users.InvalidUserCredentialsException
import nl.jovmit.lyrics.domain.users.LoginService
import nl.jovmit.lyrics.domain.users.UserCredentials
import nl.jovmit.lyrics.infrastructure.json.UserJson.jsonFor
import org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400
import org.eclipse.jetty.http.HttpStatus.OK_200
import spark.Request
import spark.Response

class LoginApi(
    private val loginService: LoginService
) {

    fun login(request: Request, response: Response): String {
        val userCredentials = userCredentialsFrom(request)
        return try {
            val user = loginService.login(userCredentials)
            response.status(OK_200)
            response.type("application/json")
            jsonFor(user)
        } catch (exception: InvalidUserCredentialsException) {
            response.status(BAD_REQUEST_400)
            "Invalid credentials."
        }
    }

    private fun userCredentialsFrom(request: Request): UserCredentials {
        val jsonObject = JsonObject.readFrom(request.body())
        return UserCredentials(
            jsonObject.get("username").asString(),
            jsonObject.get("password").asString()
        )
    }
}
