package tk.atna.shortlyapp.presentation.main

import tk.atna.shortlyapp.domain.model.ShortenedUrl
import tk.atna.shortlyapp.presentation.model.ShortenedUrlItem

object FakeShortenedUrl {

    val url1
        get() = ShortenedUrl(
            code = "code_1",
            originalLink = "original_link_1",
            shortLink = "short_link_1"
        )
    val url2
        get() = ShortenedUrl(
            code = "code_2",
            originalLink = "original_link_2",
            shortLink = "short_link_2"
        )

    val item1
        get() = ShortenedUrlItem(url1, null)
    val item2
        get() = ShortenedUrlItem(url2, null)
}