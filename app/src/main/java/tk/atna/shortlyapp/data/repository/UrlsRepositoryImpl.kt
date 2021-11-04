package tk.atna.shortlyapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tk.atna.shortlyapp.data.datasource.api.ServerApi
import tk.atna.shortlyapp.data.datasource.db.ShortenedUrlsDao
import tk.atna.shortlyapp.data.model.toEntity
import tk.atna.shortlyapp.data.model.toModel
import tk.atna.shortlyapp.domain.model.ShortenedUrl
import tk.atna.shortlyapp.domain.repository.UrlsRepository

class UrlsRepositoryImpl(
    private val urlsDao: ShortenedUrlsDao,
    private val serverApi: ServerApi
) : UrlsRepository {

    override fun getShortenedUrls(): Flow<List<ShortenedUrl>> =
        urlsDao.getShortenedUrls().map { it.toModel() }

    override suspend fun deleteShortenedUrl(shortenedUrl: ShortenedUrl) =
        urlsDao.delete(shortenedUrl.code)

    override suspend fun shortenUrl(url: String) {
        serverApi.shortenUrl(url)
            .result
            .also { urlsDao.insert(it.toEntity(System.currentTimeMillis())) }
    }
}