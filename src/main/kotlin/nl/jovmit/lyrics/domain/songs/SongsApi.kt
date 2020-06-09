package nl.jovmit.lyrics.domain.songs

import com.eclipsesource.json.JsonObject
import org.eclipse.jetty.http.HttpStatus.CREATED_201
import spark.Request
import spark.Response

class SongsApi(
    private val songsService: SongService
) {

    fun createSong(request: Request, response: Response): String {
        val userId = request.params("userId")
        val songData = songDataFrom(request)
        val song = songsService.createSong(userId, songData)
        response.status(CREATED_201)
        response.type("application/json")
        return jsonFor(song)
    }

    private fun jsonFor(song: Song): String {
        return JsonObject()
            .add("userId", song.userId)
            .add("songId", song.songId)
            .add("songTitle", song.title)
            .add("songPerformer", song.performer)
            .add("songLyrics", song.lyrics)
            .toString()
    }

    private fun songDataFrom(request: Request): SongData {
        val jsonObject = JsonObject.readFrom(request.body())
        return SongData(
            jsonObject.get("songTitle").asString(),
            jsonObject.get("songPerformer").asString(),
            jsonObject.get("songLyrics").asString()
        )
    }
}
