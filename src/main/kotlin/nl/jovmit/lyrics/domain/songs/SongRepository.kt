package nl.jovmit.lyrics.domain.songs

import java.util.*

class SongRepository {

    private val songs = mutableListOf<Song>()

    fun add(song: Song) {
        songs.add(song)
    }

    fun songsFor(userId: String): List<Song> {
        return songs.filter { it.userId == userId }
    }

    fun getSong(userId: String, songId: String): Optional<Song> {
        TODO("not implemented")
    }

    fun update(updatedSong: Song) {
        TODO("not implemented")
    }
}
