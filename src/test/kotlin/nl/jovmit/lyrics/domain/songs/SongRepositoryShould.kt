package nl.jovmit.lyrics.domain.songs

import nl.jovmit.lyrics.infrastructure.builder.SongBuilder.Companion.aSong
import nl.jovmit.lyrics.infrastructure.builder.UserBuilder.Companion.aUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SongRepositoryShould {

    private val tom = aUser().build()
    private val mile = aUser().build()
    private val tomSongOne = aSong().withUserId(tom.id).build()
    private val mileSongOne = aSong().withUserId(mile.id).build()
    private val tomSongTwo = aSong().withUserId(tom.id).build()
    private val mileSongTwo = aSong().withUserId(mile.id).build()

    private val songRepository = SongRepository()

    @Test
    fun return_songs_for_a_given_user() {
        songRepository.add(tomSongOne)
        songRepository.add(mileSongOne)
        songRepository.add(tomSongTwo)
        songRepository.add(mileSongTwo)

        val result = songRepository.songsFor(tom.id)

        assertThat(result).containsExactly(tomSongOne, tomSongTwo)
    }

    @Test
    fun return_song_matching_correct_ids() {
        songRepository.add(tomSongOne)
        songRepository.add(mileSongOne)

        assertThat(songRepository.getSong(tom.id, tomSongOne.songId)).isPresent()
        assertThat(songRepository.getSong(tom.id, mileSongOne.songId)).isEmpty()
    }

    @Test
    fun update_existing_song() {
        songRepository.add(tomSongOne)
        val tomSongOneUpdated = tomSongOne.copy(title = "updated")

        songRepository.update(tomSongOneUpdated)
        val result = songRepository.getSong(tom.id, tomSongOne.songId).get()

        assertThat(result).isEqualTo(tomSongOneUpdated)
    }
}