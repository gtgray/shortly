package tk.atna.shortlyapp.domain.repository

import kotlinx.coroutines.flow.Flow
import tk.atna.shortlyapp.domain.model.ShortenedUrl

interface UrlsRepository {
    fun getShortenedUrls(): Flow<List<ShortenedUrl>>
    suspend fun deleteShortenedUrl(shortenedUrl: ShortenedUrl)
    suspend fun shortenUrl(url: String)
}