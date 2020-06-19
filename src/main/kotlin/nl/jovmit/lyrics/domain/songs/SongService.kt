package nl.jovmit.lyrics.domain.songs

import nl.jovmit.lyrics.domain.users.UnknownUserException
import nl.jovmit.lyrics.domain.users.UserRepository
import nl.jovmit.lyrics.infrastructure.utils.IdGenerator

class SongService(
    private val idGenerator: IdGenerator,
    private val songRepository: SongRepository,
    private val userRepository: UserRepository
) {

    fun createSong(userId: String, songData: SongData): Song {
        validate(userId)
        val songId = idGenerator.next()
        val song = Song(userId, songId, songData.title, songData.performer, songData.lyrics)
        songRepository.add(song)
        return song
    }

    fun songsFor(userId: String): List<Song> {
        validate(userId)
        return songRepository.songsFor(userId)
    }

    fun editSong(userId: String, songId: String, songData: SongData): Song {
        validate(userId)
        val song = songRepository.getSong(userId, songId)
        if (song.isPresent) {
            val updatedSong = song.get()
                .copy(title = songData.title, performer = songData.performer, lyrics = songData.lyrics)
            songRepository.update(updatedSong)
            return updatedSong
        }
        throw UnknownSongException()
    }

    private fun validate(userId: String) {
        if (!userRepository.hasUserWithId(userId)) {
            throw UnknownUserException()
        }
    }
}
