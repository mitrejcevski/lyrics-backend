package nl.jovmit.lyrics.api

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.search.SearchService
import nl.jovmit.lyrics.domain.songs.Song
import nl.jovmit.lyrics.domain.users.UnknownUserException
import nl.jovmit.lyrics.infrastructure.builder.SongBuilder.Companion.aSong
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willReturn
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import spark.Request
import spark.Response
import java.util.*

@ExtendWith(MockitoExtension::class)
class SearchApiShould {

    @Mock
    private lateinit var searchService: SearchService

    @Mock
    private lateinit var request: Request

    @Mock
    private lateinit var response: Response

    private val userId = UUID.randomUUID().toString()
    private val song = aSong().build()
    private val songs = listOf(song)
    private val searchKeyword = song.title

    private lateinit var searchApi: SearchApi

    @BeforeEach
    fun setUp() {
        searchApi = SearchApi(searchService)
        willReturn(userId).given(request).params("userId")
        willReturn(searchKeyword).given(request).params("keyword")
    }

    @Test
    fun return_json_containing_search_results() {
        given(searchService.searchSongs(userId, searchKeyword)).willReturn(songs)

        val result = searchApi.searchSong(request, response)

        verify(response).status(200)
        verify(response).type("application/json")
        assertThat(result).isEqualTo(jsonContaining(songs))
    }

    @Test
    fun return_error_when_unknown_user_makes_a_search() {
        given(searchService.searchSongs(userId, searchKeyword)).willThrow(UnknownUserException::class.java)

        val result = searchApi.searchSong(request, response)

        verify(response).status(400)
        assertThat(result).isEqualTo("The user does not exist.")
    }

    private fun jsonContaining(songs: List<Song>): String {
        val jsonArray = JsonArray()
        songs.forEach { jsonArray.add(jsonObjectFor(it)) }
        return jsonArray.toString()
    }

    private fun jsonObjectFor(song: Song): JsonObject? {
        return JsonObject()
            .add("userId", song.userId)
            .add("songId", song.songId)
            .add("songTitle", song.title)
            .add("songPerformer", song.performer)
            .add("songLyrics", song.lyrics)
    }
}