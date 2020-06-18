package nl.jovmit.lyrics.domain.songs

class SongRepository {

    private val songs = mutableListOf<Song>()

    fun add(song: Song) {
        songs.add(song)
    }

    fun songsFor(userId: String): List<Song> {
        return songs.filter { it.userId == userId }
    }
}
