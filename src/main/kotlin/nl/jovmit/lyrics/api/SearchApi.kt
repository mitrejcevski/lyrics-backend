package nl.jovmit.lyrics.api

import nl.jovmit.lyrics.domain.search.SearchService
import nl.jovmit.lyrics.domain.users.UnknownUserException
import nl.jovmit.lyrics.infrastructure.json.SongJson.jsonFor
import org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400
import org.eclipse.jetty.http.HttpStatus.OK_200
import spark.Request
import spark.Response

class SearchApi(
    private val searchService: SearchService
) {

    fun searchSong(request: Request, response: Response): String {
        val userId = request.params("userId")
        val keyword = request.params("keyword")
        return try {
            val result = searchService.searchSongs(userId, keyword)
            response.type("application/json")
            response.status(OK_200)
            jsonFor(result)
        } catch (unknownUserException: UnknownUserException) {
            response.status(BAD_REQUEST_400)
            "The user does not exist."
        }
    }
}
