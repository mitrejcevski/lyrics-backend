package nl.jovmit.lyrics.domain.songs

import nl.jovmit.lyrics.domain.users.UnknownUserException
import nl.jovmit.lyrics.domain.users.UserRepository
import nl.jovmit.lyrics.infrastructure.builder.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.infrastructure.builder.SongDataBuilder.Companion.songData
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
    private val songData = songData().build()
    private val updatedSongData = songData().withTitle("new").withPerformer("new").withLyrics("new").build()
    private val song = aSong().withUserId(userId).withSongId(songId).build()
    private val updatedSong = aSong().withUserId(userId).withSongId(songId).withSongData(updatedSongData).build()
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

    @Test
    fun edit_a_song() {
        given(songRepository.getSong(userId, songId)).willReturn(Optional.of(song))

        val result = songService.editSong(userId, songId, updatedSongData)

        verify(songRepository).update(updatedSong)
        assertThat(result).isEqualTo(updatedSong)
    }

    @Test
    fun throw_exception_when_unknown_user_tries_to_update_a_song() {
        given(userRepository.hasUserWithId(userId)).willReturn(false)

        assertThrows<UnknownUserException> {
            songService.editSong(userId, songId, updatedSongData)
        }
    }

    @Test
    fun throw_exception_when_attempting_to_update_an_unknown_song() {
        given(songRepository.getSong(userId, songId)).willReturn(Optional.empty())

        assertThrows<UnknownSongException> {
            songService.editSong(userId, songId, updatedSongData)
        }
    }

    @Test
    fun delete_a_song() {
        given(songRepository.getSong(userId, songId)).willReturn(Optional.of(song))

        val result = songService.deleteSong(userId, songId)

        verify(songRepository).delete(song)
        assertThat(result).isEqualTo(song.songId)
    }

    @Test
    fun throw_exception_when_unknown_user_tries_to_delete_a_song() {
        given(userRepository.hasUserWithId(userId)).willReturn(false)

        assertThrows<UnknownUserException> {
            songService.deleteSong(userId, songId)
        }
    }

    @Test
    fun throw_exception_when_attempting_to_delete_an_unknown_song() {
        given(songRepository.getSong(userId, songId)).willReturn(Optional.empty())

        assertThrows<UnknownSongException> {
            songService.deleteSong(userId, songId)
        }
    }

    @Test
    fun return_a_song_for_given_id() {
        given(songRepository.getSong(userId, songId)).willReturn(Optional.of(song))

        val result = songService.songFor(userId, songId)

        assertThat(result).isEqualTo(song)
    }

    @Test
    fun throw_exception_when_unknown_user_tries_to_get_song_by_id() {
        given(userRepository.hasUserWithId(userId)).willReturn(false)

        assertThrows<UnknownUserException> {
            songService.songFor(userId, songId)
        }
    }

    @Test
    fun throw_exception_when_trying_to_get_a_non_existing_song() {
        given(songRepository.getSong(userId, songId)).willReturn(Optional.empty())

        assertThrows<UnknownSongException> {
            songService.songFor(userId, songId)
        }
    }
}