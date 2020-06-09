package nl.jovmit.lyrics.infrastructure.json

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.songs.Song

object SongJson {

    fun jsonFor(song: Song): String {
        return JsonObject()
            .add("userId", song.userId)
            .add("songId", song.songId)
            .add("songTitle", song.title)
            .add("songPerformer", song.performer)
            .add("songLyrics", song.lyrics)
            .toString()
    }
}