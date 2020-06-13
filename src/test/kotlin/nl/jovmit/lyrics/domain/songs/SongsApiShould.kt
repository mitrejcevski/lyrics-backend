package nl.jovmit.lyrics.domain.songs

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonObject
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
class SongsApiShould {

    @Mock
    private lateinit var request: Request

    @Mock
    private lateinit var response: Response

    @Mock
    private lateinit var songsService: SongService

    private val userId = UUID.randomUUID().toString()
    private val songId = UUID.randomUUID().toString()
    private val songTitle = "title"
    private val songPerformer = "performer"
    private val songLyrics = "lyrics"
    private val songData = SongData(songTitle, songPerformer, songLyrics)
    private val song = Song(userId, songId, songTitle, songPerformer, songLyrics)
    private val songs = listOf(song)

    private lateinit var songsApi: SongsApi

    @BeforeEach
    fun setUp() {
        songsApi = SongsApi(songsService)
        given(request.params("userId")).willReturn(userId)
    }

    @Test
    fun create_a_song() {
        given(request.body()).willReturn(jsonContaining(songData))
        given(songsService.createSong(userId, songData)).willReturn(song)

        songsApi.createSong(request, response)

        verify(songsService).createSong(userId, songData)
    }

    @Test
    fun return_json_containing_created_song() {
        given(request.body()).willReturn(jsonContaining(songData))
        given(songsService.createSong(userId, songData)).willReturn(song)

        val result = songsApi.createSong(request, response)

        verify(response).status(201)
        verify(response).type("application/json")
        assertThat(result).isEqualTo(jsonContaining(song))
    }

    @Test
    fun return_json_containing_songs_for_a_given_user() {
        given(request.params("userId")).willReturn(userId)
        given(songsService.songsFor(userId)).willReturn(songs)

        val result = songsApi.songsByUser(request, response)

        verify(response).status(200)
        verify(response).type("application/json")
        assertThat(result).isEqualTo(jsonContaining(songs))
    }

    @Test
    fun return_error_when_attempting_to_load_songs_for_unknown_user() {
        given(request.params("userId")).willReturn(userId)
        given(songsService.songsFor(userId)).willThrow(UnknownUserException::class.java)

        val result = songsApi.songsByUser(request, response)

        verify(response).status(400)
        assertThat(result).isEqualTo("The user does not exist.")
    }

    private fun jsonContaining(songs: List<Song>): String {
        val jsonArray = JsonArray()
        songs.forEach { jsonArray.add(jsonObjectFor(it)) }
        return jsonArray.toString()
    }

    private fun jsonContaining(song: Song): String {
        return jsonObjectFor(song).toString()
    }

    private fun jsonObjectFor(song: Song): JsonObject? {
        return JsonObject()
            .add("userId", song.userId)
            .add("songId", song.songId)
            .add("songTitle", song.title)
            .add("songPerformer", song.performer)
            .add("songLyrics", song.lyrics)
    }

    private fun jsonContaining(songData: SongData): String {
        return JsonObject()
            .add("songTitle", songData.title)
            .add("songPerformer", songData.performer)
            .add("songLyrics", songData.lyrics)
            .toString()
    }
}