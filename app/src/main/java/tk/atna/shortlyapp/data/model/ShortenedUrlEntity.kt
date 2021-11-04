package tk.atna.shortlyapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import tk.atna.shortlyapp.domain.model.ShortenedUrl

@Entity(tableName = "shortened_urls")
data class ShortenedUrlEntity(
    @PrimaryKey
    val code: String,
    val originalLink: String,
    val shortLink: String,
    val timestamp: Long
)

fun ShortenedUrlEntity.toModel() = ShortenedUrl(
    code = code,
    originalLink = originalLink,
    shortLink = shortLink
)

fun List<ShortenedUrlEntity>.toModel() = map { it.toModel() }