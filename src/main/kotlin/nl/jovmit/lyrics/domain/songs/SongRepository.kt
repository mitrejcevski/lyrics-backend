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
        val matchingSong = songs.firstOrNull { it.matchesIds(userId, songId) }
        if (matchingSong != null) {
            return Optional.of(matchingSong)
        }
        return Optional.empty()
    }

    fun update(updatedSong: Song) {
        val index = songs.indexOfFirst { it.matchesIds(updatedSong.userId, updatedSong.songId) }
        if (index != -1) {
            songs[index] = updatedSong
        }
    }
}
