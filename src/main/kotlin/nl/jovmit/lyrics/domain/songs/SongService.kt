package nl.jovmit.lyrics.domain.songs

import nl.jovmit.lyrics.infrastructure.utils.IdGenerator

class SongService(
    private val idGenerator: IdGenerator,
    private val songRepository: SongRepository
) {

    fun createSong(userId: String, songData: SongData): Song {
        val songId = idGenerator.next()
        val song = Song(userId, songId, songData.title, songData.performer, songData.lyrics)
        songRepository.add(song)
        return song
    }
}
