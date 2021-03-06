package nl.jovmit.lyrics.api

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.users.UserData
import nl.jovmit.lyrics.domain.users.UserService
import nl.jovmit.lyrics.domain.users.UsernameAlreadyInUseException
import nl.jovmit.lyrics.infrastructure.json.UserJson.jsonFor
import org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400
import org.eclipse.jetty.http.HttpStatus.CREATED_201
import spark.Request
import spark.Response

class UsersApi(
    private val userService: UserService
) {

    fun createUser(request: Request, response: Response): String {
        val userData = userDataFrom(request)
        return try {
            val user = userService.createUser(userData)
            response.status(CREATED_201)
            response.type("application/json")
            jsonFor(user)
        } catch (exception: UsernameAlreadyInUseException) {
            response.status(BAD_REQUEST_400)
            "Username already in use."
        }
    }

    private fun userDataFrom(request: Request): UserData {
        val jsonObject = JsonObject.readFrom(request.body())
        return UserData(
            jsonObject.get("username").asString(),
            jsonObject.get("password").asString(),
            jsonObject.get("about").asString()
        )
    }
}
