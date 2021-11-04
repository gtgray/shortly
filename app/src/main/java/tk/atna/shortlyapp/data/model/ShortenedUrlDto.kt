package tk.atna.shortlyapp.data.model

import com.google.gson.annotations.SerializedName

data class ShortenedUrlDto(
    @SerializedName("code")
    val code: String,
    @SerializedName("original_link")
    val originalLink: String,
    @SerializedName("full_short_link")
    val shortLink: String
)

fun ShortenedUrlDto.toEntity(timestamp: Long) = ShortenedUrlEntity(
    code = code,
    originalLink = originalLink,
    shortLink = shortLink,
    timestamp = timestamp
)