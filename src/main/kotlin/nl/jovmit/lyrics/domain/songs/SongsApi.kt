package nl.jovmit.lyrics.domain.songs

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.infrastructure.json.SongJson.jsonFor
import org.eclipse.jetty.http.HttpStatus.*
import spark.Request
import spark.Response

class SongsApi(
    private val songService: SongService
) {

    fun createSong(request: Request, response: Response): String {
        val userId = request.params("userId")
        return try {
            val songData = songDataFrom(request)
            val song = songService.createSong(userId, songData)
            response.status(CREATED_201)
            response.type("application/json")
            jsonFor(song)
        } catch (unknownUserException: UnknownUserException) {
            response.status(BAD_REQUEST_400)
            "The user does not exist."
        }
    }

    fun songsByUser(request: Request, response: Response): String {
        val userId = request.params("userId")
        return try {
            val songs = songService.songsFor(userId)
            response.status(OK_200)
            response.type("application/json")
            jsonFor(songs)
        } catch (unknownUserException: UnknownUserException) {
            response.status(BAD_REQUEST_400)
            "The user does not exist."
        }
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
