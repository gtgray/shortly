package tk.atna.shortlyapp.domain.interactor

import kotlinx.coroutines.flow.Flow
import tk.atna.shortlyapp.domain.model.ShortenedUrl
import tk.atna.shortlyapp.domain.repository.UrlsRepository

class UrlsInteractor(
    private val urlsRepository: UrlsRepository
) {

    fun getShortenedUrls(): Flow<List<ShortenedUrl>> =
        urlsRepository.getShortenedUrls()

    suspend fun deleteShortenedUrl(shortenedUrl: ShortenedUrl) =
        urlsRepository.deleteShortenedUrl(shortenedUrl)

    suspend fun shortenUrl(url: String) =
        urlsRepository.shortenUrl(url)
}