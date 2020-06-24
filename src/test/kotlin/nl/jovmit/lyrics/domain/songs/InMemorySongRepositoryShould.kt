package nl.jovmit.lyrics.domain.songs

class InMemorySongRepositoryShould : SongRepositoryContract() {

    override fun repositoryWith(vararg songs: Song): SongRepository {
        return InMemorySongRepository().apply {
            songs.forEach { add(it) }
        }
    }
}