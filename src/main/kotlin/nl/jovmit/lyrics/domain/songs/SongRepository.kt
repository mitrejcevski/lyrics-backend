package nl.jovmit.lyrics.domain.songs

import java.util.*

interface SongRepository {

    fun add(song: Song)

    fun songsFor(userId: String): List<Song>

    fun getSong(userId: String, songId: String): Optional<Song>

    fun update(updatedSong: Song)

    fun delete(song: Song)
}