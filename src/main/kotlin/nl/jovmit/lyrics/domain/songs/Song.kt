package nl.jovmit.lyrics.domain.songs

data class Song(
    val userId: String,
    val songId: String,
    val title: String,
    val performer: String,
    val lyrics: String
) {

    fun matchesIds(userId: String, songId: String): Boolean {
        return this.userId == userId && this.songId == songId
    }

    fun containsKeyword(keyword: String): Boolean {
        return title.contains(keyword, ignoreCase = true) ||
                performer.contains(keyword, ignoreCase = true) ||
                lyrics.contains(keyword, ignoreCase = true)
    }
}
