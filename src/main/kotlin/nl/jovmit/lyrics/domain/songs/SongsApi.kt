package nl.jovmit.lyrics.domain.songs

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.infrastructure.json.SongJson.jsonFor
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

    private fun songDataFrom(request: Request): SongData {
        val jsonObject = JsonObject.readFrom(request.body())
        return SongData(
            jsonObject.get("songTitle").asString(),
            jsonObject.get("songPerformer").asString(),
            jsonObject.get("songLyrics").asString()
        )
    }
}
