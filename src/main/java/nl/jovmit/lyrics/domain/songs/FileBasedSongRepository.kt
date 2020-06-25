package nl.jovmit.lyrics.domain.songs

import com.eclipsesource.json.JsonArray
import com.eclipsesource.json.JsonObject
import nl.jovmit.lyrics.infrastructure.utils.Directories.SONGS_FILE_NAME
import nl.jovmit.lyrics.infrastructure.utils.Directories.USERS_DIR
import java.io.File
import java.util.*

class FileBasedSongRepository : SongRepository {

    private val usersRoot: File = File(USERS_DIR).also { it.mkdirs() }

    override fun add(song: Song) {
        val songs = readSongsFor(song.userId)
        songs.add(song)
        writeSongsFor(song.userId, songs)
    }

    override fun songsFor(userId: String): List<Song> {
        return readSongsFor(userId)
    }

    override fun getSong(userId: String, songId: String): Optional<Song> {
        val songs = readSongsFor(userId)
        val matchingSong = songs.firstOrNull { it.matchesIds(userId, songId) }
        return if (matchingSong != null) Optional.of(matchingSong) else Optional.empty()
    }

    override fun update(updatedSong: Song) {
        val songs = readSongsFor(updatedSong.userId)
        val index = songs.indexOfFirst { it.matchesIds(updatedSong.userId, updatedSong.songId) }
        if (index != -1) {
            songs[index] = updatedSong
            writeSongsFor(updatedSong.userId, songs)
        }
    }

    override fun delete(song: Song) {
        val songs = readSongsFor(song.userId)
        val songIndex = songs.indexOf(song)
        if (songIndex != -1) {
            songs.removeAt(songIndex)
            writeSongsFor(song.userId, songs)
        }
    }

    private fun readSongsFor(userId: String): MutableList<Song> {
        val songsFile = File(usersRoot, "$userId/$SONGS_FILE_NAME")
        return if (!songsFile.exists()) {
            mutableListOf()
        } else {
            val jsonArray = JsonArray.readFrom(songsFile.readText())
            jsonArray.map { jsonSong -> songFrom(jsonSong.asObject()) }.toMutableList()
        }
    }

    private fun writeSongsFor(userId: String, songs: List<Song>) {
        val jsonArray = JsonArray()
        songs.forEach { song -> jsonArray.add(jsonFor(song)) }
        val userDirectory = File(usersRoot, userId).apply { mkdirs() }
        val songsFile = File(userDirectory, SONGS_FILE_NAME)
        if (!songsFile.exists()) {
            songsFile.createNewFile()
        }
        songsFile.writeText(jsonArray.toString())
    }

    private fun jsonFor(song: Song): JsonObject {
        return JsonObject()
            .add("userId", song.userId)
            .add("songId", song.songId)
            .add("songTitle", song.title)
            .add("songPerformer", song.performer)
            .add("songLyrics", song.lyrics)
    }

    private fun songFrom(jsonSong: JsonObject): Song {
        return Song(
            jsonSong.get("userId").asString(),
            jsonSong.get("songId").asString(),
            jsonSong.get("songTitle").asString(),
            jsonSong.get("songPerformer").asString(),
            jsonSong.get("songLyrics").asString()
        )
    }
}
