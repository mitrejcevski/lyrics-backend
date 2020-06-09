package nl.jovmit.lyrics.api

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.users.User
import nl.jovmit.lyrics.domain.users.UserData
import nl.jovmit.lyrics.domain.users.UserService
import nl.jovmit.lyrics.domain.users.UsernameAlreadyInUseException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import spark.Request
import spark.Response
import java.util.*

@ExtendWith(MockitoExtension::class)
class UsersApiShould {

    companion object {
        private val USER_ID = UUID.randomUUID().toString()
        private const val USERNAME = "Tom"
        private const val PASSWORD = "alksjd12ealksjd"
        private const val ABOUT = "About Tom"
        private val USER_DATA = UserData(USERNAME, PASSWORD, ABOUT)
        private val USER = User(USER_ID, USERNAME, PASSWORD, ABOUT)
    }

    @Mock
    private lateinit var response: Response

    @Mock
    private lateinit var request: Request

    @Mock
    private lateinit var userService: UserService

    private lateinit var usersApi: UsersApi

    @BeforeEach
    fun setUp() {
        usersApi = UsersApi(userService)
    }

    @Test
    fun create_a_user() {
        given(userService.createUser(USER_DATA)).willReturn(USER)
        given(request.body()).willReturn(jsonContaining(USER_DATA))

        usersApi.createUser(request, response)

        verify(userService).createUser(USER_DATA)
    }

    @Test
    fun return_json_containing_newly_created_user() {
        given(request.body()).willReturn(jsonContaining(USER_DATA))
        given(userService.createUser(USER_DATA)).willReturn(USER)

        val result = usersApi.createUser(request, response)

        verify(response).status(201)
        verify(response).type("application/json")
        assertThat(jsonContaining(USER)).isEqualTo(result)
    }

    @Test
    fun return_error_when_creating_a_duplicate_user() {
        given(request.body()).willReturn(jsonContaining(USER_DATA))
        given(userService.createUser(USER_DATA)).willThrow(UsernameAlreadyInUseException::class.java)

        val result = usersApi.createUser(request, response)

        verify(response).status(400)
        assertThat(result).isEqualTo("Username already in use.")
    }

    private fun jsonContaining(user: User): String {
        return JsonObject()
            .add("id", user.id)
            .add("username", user.username)
            .add("about", user.about)
            .toString()
    }

    private fun jsonContaining(userData: UserData): String {
        return JsonObject()
            .add("username", userData.username)
            .add("password", userData.password)
            .add("about", userData.about)
            .toString()
    }
}