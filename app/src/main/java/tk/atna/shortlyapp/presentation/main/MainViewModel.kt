package tk.atna.shortlyapp.presentation.main

import kotlinx.coroutines.launch
import tk.atna.shortlyapp.domain.interactor.UrlsInteractor
import tk.atna.shortlyapp.domain.model.ShortenedUrl
import tk.atna.shortlyapp.presentation.base.BaseViewModel

class MainViewModel(
    private val urlsInteractor: UrlsInteractor
) : BaseViewModel() {

    // todo:
    //  val history: List<ShortenedUrl> = urlsInteractor.getShortenedUrls()

    fun copyUrl(shortenedUrl: ShortenedUrl) {
        // todo: cache
    }

    fun deleteUrl(shortenedUrl: ShortenedUrl) {
        launch {
            urlsInteractor.deleteShortenedUrl(shortenedUrl)
        }
    }

    fun shortenUrl(url: String) {
        launch {
            urlsInteractor.shortenUrl(url)
        }
    }
}