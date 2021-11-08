package tk.atna.shortlyapp.domain.model

data class ShortenedUrl(
    val code: String,
    val originalLink: String,
    val shortLink: String
)