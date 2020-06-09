package nl.jovmit.lyrics.domain.songs

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

    private lateinit var songsApi: SongsApi

    @BeforeEach
    fun setUp() {
        songsApi = SongsApi(songsService)
    }

    @Test
    fun create_a_song() {
        given(request.params("userId")).willReturn(userId)
        given(request.body()).willReturn(jsonContaining(songData))
        given(songsService.createSong(userId, songData)).willReturn(song)

        songsApi.createSong(request, response)

        verify(songsService).createSong(userId, songData)
    }

    @Test
    fun return_json_containing_created_song() {
        given(request.params("userId")).willReturn(userId)
        given(request.body()).willReturn(jsonContaining(songData))
        given(songsService.createSong(userId, songData)).willReturn(song)

        val result = songsApi.createSong(request, response)

        verify(response).status(201)
        verify(response).type("application/json")
        assertThat(result).isEqualTo(jsonContaining(song))
    }

    private fun jsonContaining(song: Song): String {
        return JsonObject()
            .add("userId", song.userId)
            .add("songId", song.songId)
            .add("songTitle", song.title)
            .add("songPerformer", song.performer)
            .add("songLyrics", song.lyrics)
            .toString()
    }

    private fun jsonContaining(songData: SongData): String {
        return JsonObject()
            .add("songTitle", songData.title)
            .add("songPerformer", songData.performer)
            .add("songLyrics", songData.lyrics)
            .toString()
    }
}