package nl.jovmit.lyrics.infrastructure.json

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.users.User

object UserJson {

    fun jsonFor(user: User): String {
        return JsonObject()
            .add("id", user.id)
            .add("username", user.username)
            .add("about", user.about)
            .toString()
    }
}