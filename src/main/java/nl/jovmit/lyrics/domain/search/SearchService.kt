package nl.jovmit.lyrics.domain.search

import nl.jovmit.lyrics.domain.songs.Song
import nl.jovmit.lyrics.domain.songs.SongRepository
import nl.jovmit.lyrics.domain.users.UnknownUserException
import nl.jovmit.lyrics.domain.users.UserRepository

class SearchService(
    private val songRepository: SongRepository,
    private val userRepository: UserRepository
) {

    fun searchSongs(userId: String, searchKeyword: String): List<Song> {
        validate(userId)
        val songs = songRepository.songsFor(userId)
        return songs.filter { it.containsKeyword(searchKeyword) }
    }

    private fun validate(userId: String) {
        if (!userRepository.hasUserWithId(userId)) {
            throw UnknownUserException()
        }
    }
}
