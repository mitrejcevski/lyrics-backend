package nl.jovmit.lyrics.domain.songs

import nl.jovmit.lyrics.infrastructure.builder.SongBuilder
import nl.jovmit.lyrics.infrastructure.builder.UserBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

abstract class SongRepositoryContract {

    private val tom = UserBuilder.aUser().build()
    private val mile = UserBuilder.aUser().build()
    private val tomSongOne = SongBuilder.aSong().withUserId(tom.id).build()
    private val mileSongOne = SongBuilder.aSong().withUserId(mile.id).build()
    private val tomSongTwo = SongBuilder.aSong().withUserId(tom.id).build()
    private val mileSongTwo = SongBuilder.aSong().withUserId(mile.id).build()

    @Test
    fun return_songs_for_a_given_user() {
        val songRepository = repositoryWith(tomSongOne, mileSongOne, tomSongTwo, mileSongTwo)

        val result = songRepository.songsFor(tom.id)

        assertThat(result).containsExactly(tomSongOne, tomSongTwo)
    }

    @Test
    fun return_song_matching_correct_ids() {
        val songRepository = repositoryWith(tomSongOne, mileSongOne)

        assertThat(songRepository.getSong(tom.id, tomSongOne.songId)).isPresent()
        assertThat(songRepository.getSong(tom.id, mileSongOne.songId)).isEmpty()
    }

    @Test
    fun update_existing_song() {
        val songRepository = repositoryWith(tomSongOne)
        val tomSongOneUpdated = tomSongOne.copy(title = "updated")

        songRepository.update(tomSongOneUpdated)
        val result = songRepository.getSong(tom.id, tomSongOne.songId).get()

        assertThat(result).isEqualTo(tomSongOneUpdated)
    }

    @Test
    fun delete_song() {
        val songRepository = repositoryWith(tomSongOne, tomSongTwo)

        songRepository.delete(tomSongOne)

        assertThat(songRepository.getSong(tom.id, tomSongOne.songId)).isEmpty()
        assertThat(songRepository.getSong(tom.id, tomSongTwo.songId)).isPresent()
    }

    abstract fun repositoryWith(vararg songs: Song): SongRepository
}