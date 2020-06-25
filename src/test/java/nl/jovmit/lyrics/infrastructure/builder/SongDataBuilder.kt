package nl.jovmit.lyrics.infrastructure.builder

import nl.jovmit.lyrics.domain.songs.SongData

class SongDataBuilder {

    companion object {
        fun songData(): SongDataBuilder {
            return SongDataBuilder()
        }
    }

    private var title = "title"
    private var performer = "performer"
    private var lyrics = "lyrics"

    fun withTitle(title: String): SongDataBuilder {
        this.title = title
        return this
    }

    fun withPerformer(performer: String): SongDataBuilder {
        this.performer = performer
        return this
    }

    fun withLyrics(lyrics: String): SongDataBuilder {
        this.lyrics = lyrics
        return this
    }

    fun build(): SongData {
        return SongData(title, performer, lyrics)
    }
}
