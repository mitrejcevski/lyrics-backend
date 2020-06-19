package nl.jovmit.lyrics.domain.songs

import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.domain.users.UnknownUserException
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
            prepareUnknownUserError(response)
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
            prepareUnknownUserError(response)
        }
    }

    fun editSong(request: Request, response: Response): String {
        val userId = request.params("userId")
        val songId = request.params("songId")
        val songData = songDataFrom(request)
        return try {
            prepareOkResponse(userId, songId, songData, response)
        } catch (unknownSongException: UnknownSongException) {
            prepareUnknownSongError(response)
        } catch (unknownUserException: UnknownUserException) {
            prepareUnknownUserError(response)
        }
    }

    private fun prepareUnknownSongError(response: Response): String {
        response.status(BAD_REQUEST_400)
        return "The song does not exist."
    }

    private fun prepareUnknownUserError(response: Response): String {
        response.status(BAD_REQUEST_400)
        return "The user does not exist."
    }

    private fun prepareOkResponse(
        userId: String,
        songId: String,
        songData: SongData,
        response: Response
    ): String {
        val song = songService.editSong(userId, songId, songData)
        response.status(ACCEPTED_202)
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
