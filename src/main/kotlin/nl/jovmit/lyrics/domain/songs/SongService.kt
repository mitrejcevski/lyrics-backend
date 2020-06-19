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

    private fun validate(userId: String) {
        if (!userRepository.hasUserWithId(userId)) {
            throw UnknownUserException()
        }
    }

    fun editSong(userId: String, songId: String, songData: SongData): Song {
        TODO("not implemented")
    }
}
