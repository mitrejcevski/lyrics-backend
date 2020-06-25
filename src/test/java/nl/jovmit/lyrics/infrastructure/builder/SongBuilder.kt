package nl.jovmit.lyrics.infrastructure.builder

import nl.jovmit.lyrics.domain.songs.Song
import nl.jovmit.lyrics.domain.songs.SongData
import java.util.*

class SongBuilder {

    companion object {
        fun aSong(): SongBuilder {
            return SongBuilder()
        }
    }

    private var userId: String = UUID.randomUUID().toString()
    private var songId: String = UUID.randomUUID().toString()
    private var title: String = "title"
    private var performer: String = "performer"
    private var lyrics: String = "lyrics"

    fun withUserId(userId: String): SongBuilder {
        this.userId = userId
        return this
    }

    fun withSongId(songId: String): SongBuilder {
        this.songId = songId
        return this
    }

    fun withTitle(title: String): SongBuilder {
        this.title = title
        return this
    }

    fun withPerformer(performer: String): SongBuilder {
        this.performer = performer
        return this
    }

    fun withLyrics(lyrics: String): SongBuilder {
        this.lyrics = lyrics
        return this
    }

    fun withSongData(songData: SongData): SongBuilder {
        this.title = songData.title
        this.performer = songData.performer
        this.lyrics = songData.lyrics
        return this
    }

    fun build(): Song {
        return Song(userId, songId, title, performer, lyrics)
    }
}
