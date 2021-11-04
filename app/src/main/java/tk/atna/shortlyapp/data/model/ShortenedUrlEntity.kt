package tk.atna.shortlyapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shortened_urls")
data class ShortenedUrlEntity(
    @PrimaryKey
    val code: String,
    val originalLink: String,
    val shortLink: String,
    val timestamp: Long
)