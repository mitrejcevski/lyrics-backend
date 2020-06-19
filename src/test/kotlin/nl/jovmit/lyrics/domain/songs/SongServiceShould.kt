package nl.jovmit.lyrics.domain.songs

import nl.jovmit.lyrics.domain.users.UnknownUserException
import nl.jovmit.lyrics.domain.users.UserRepository
import nl.jovmit.lyrics.infrastructure.builder.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.infrastructure.builder.SongDataBuilder
import nl.jovmit.lyrics.infrastructure.utils.IdGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class SongServiceShould {

    @Mock
    private lateinit var idGenerator: IdGenerator

    @Mock
    private lateinit var songRepository: SongRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private val userId = UUID.randomUUID().toString()
    private val songId = UUID.randomUUID().toString()
    private val songData = SongDataBuilder.songData().build()
    private val song = aSong().withUserId(userId).withSongId(songId).build()
    private val songs = listOf(song)

    private lateinit var songService: SongService

    @BeforeEach
    fun setUp() {
        songService = SongService(idGenerator, songRepository, userRepository)
        given(userRepository.hasUserWithId(userId)).willReturn(true)
    }

    @Test
    fun create_new_song() {
        given(idGenerator.next()).willReturn(songId)

        val result = songService.createSong(userId, songData)

        verify(songRepository).add(song)
        assertThat(result).isEqualTo(song)
    }

    @Test
    fun throw_an_exception_when_unknown_user_creates_a_song() {
        given(userRepository.hasUserWithId(userId)).willReturn(false)

        assertThrows<UnknownUserException> {
            songService.createSong(userId, songData)
        }
    }

    @Test
    fun return_songs_for_given_user_id() {
        given(songRepository.songsFor(userId)).willReturn(songs)

        val result = songService.songsFor(userId)

        assertThat(result).containsExactly(song)
    }

    @Test
    fun throw_exception_when_unknown_user_tries_to_load_songs() {
        given(userRepository.hasUserWithId(userId)).willReturn(false)

        assertThrows<UnknownUserException> {
            songService.songsFor(userId)
        }
    }
}