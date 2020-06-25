package nl.jovmit.lyrics.domain.search

import nl.jovmit.lyrics.domain.songs.SongRepository
import nl.jovmit.lyrics.domain.users.UnknownUserException
import nl.jovmit.lyrics.domain.users.UserRepository
import nl.jovmit.lyrics.infrastructure.builder.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.infrastructure.builder.UserBuilder.Companion.aUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SearchServiceShould {

    private val tom = aUser().build()
    private val keyword = "ye"
    private val titleMatchingSong = aSong().withTitle("Yesterday").build()
    private val performerMatchingSong = aSong().withPerformer("Billye").build()
    private val lyricsMatchingSong = aSong().withLyrics("Lyrics bye bye").build()
    private val anotherSong = aSong().build()
    private val allSongs = listOf(
        titleMatchingSong,
        lyricsMatchingSong,
        performerMatchingSong,
        anotherSong
    )

    @Mock
    private lateinit var songRepository: SongRepository
    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var searchService: SearchService

    @BeforeEach
    fun setUp() {
        searchService = SearchService(songRepository, userRepository)
    }

    @Test
    fun return_a_list_of_songs_containing_search_keyword() {
        given(userRepository.hasUserWithId(tom.id)).willReturn(true)
        given(songRepository.songsFor(tom.id)).willReturn(allSongs)

        val result = searchService.searchSongs(tom.id, keyword)

        assertThat(result).containsExactlyInAnyOrder(titleMatchingSong, performerMatchingSong, lyricsMatchingSong)
    }

    @Test
    fun throw_exception_when_unknown_user_performs_a_search() {
        given(userRepository.hasUserWithId(tom.id)).willReturn(false)

        assertThrows<UnknownUserException> {
            searchService.searchSongs(tom.id, keyword)
        }
    }
}