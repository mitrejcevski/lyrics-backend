package nl.jovmit.lyrics.domain.songs

import java.util.*

class InMemorySongRepository : SongRepository {

    private val songs = mutableListOf<Song>()

    override fun add(song: Song) {
        songs.add(song)
    }

    override fun songsFor(userId: String): List<Song> {
        return songs.filter { it.userId == userId }
    }

    override fun getSong(userId: String, songId: String): Optional<Song> {
        val matchingSong = songs.firstOrNull { it.matchesIds(userId, songId) }
        if (matchingSong != null) {
            return Optional.of(matchingSong)
        }
        return Optional.empty()
    }

    override fun update(updatedSong: Song) {
        val index = songs.indexOfFirst { it.matchesIds(updatedSong.userId, updatedSong.songId) }
        if (index != -1) {
            songs[index] = updatedSong
        }
    }

    override fun delete(song: Song) {
        val index = songs.indexOf(song)
        if (index != -1) {
            songs.removeAt(index)
        }
    }
}
