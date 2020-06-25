package nl.jovmit.lyrics.domain.users

data class User(
    val id: String,
    val username: String,
    val password: String,
    val about: String
) {
    fun matchesCredentials(
        credentials: UserCredentials
    ): Boolean {
        return username == credentials.username
                && password == credentials.password
    }
}
