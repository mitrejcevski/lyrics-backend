package nl.jovmit.lyrics.domain.songs

import org.junit.jupiter.api.AfterEach
import java.io.File

class FileBasedSongRepositoryShould : SongRepositoryContract() {

    private val repository = FileBasedSongRepository()

    override fun repositoryWith(vararg songs: Song): SongRepository {
        songs.forEach {
            repository.add(it)
        }
        return repository
    }

    @AfterEach
    fun tearDown() {
        File("files/users").deleteRecursively()
    }
}