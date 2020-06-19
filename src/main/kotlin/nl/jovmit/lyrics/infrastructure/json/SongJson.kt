package nl.jovmit.lyrics.infrastructure.json

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.songs.Song

object SongJson {

    fun jsonFor(song: Song): String {
        return jsonObjectFor(song).toString()
    }

    fun jsonFor(songs: List<Song>): String {
        val jsonArray = JsonArray()
        songs.forEach { jsonArray.add(jsonObjectFor(it)) }
        return jsonArray.toString()
    }

    fun jsonFor(deletedSongId: String): String {
        return JsonObject()
            .add("songId", deletedSongId)
            .toString()
    }

    private fun jsonObjectFor(song: Song): JsonObject? {
        return JsonObject()
            .add("userId", song.userId)
            .add("songId", song.songId)
            .add("songTitle", song.title)
            .add("songPerformer", song.performer)
            .add("songLyrics", song.lyrics)
    }
}