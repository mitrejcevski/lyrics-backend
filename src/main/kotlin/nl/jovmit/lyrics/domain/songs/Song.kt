package nl.jovmit.lyrics.domain.songs

data class Song(
    val userId: String,
    val songId: String,
    val title: String,
    val performer: String,
    val lyrics: String
)
