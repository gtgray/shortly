package tk.atna.shortlyapp.presentation.model

import tk.atna.shortlyapp.domain.model.ShortenedUrl

data class ShortenedUrlItem(
    val shortenedUrl: ShortenedUrl,
    val copiedLink: String?
)