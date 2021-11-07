package tk.atna.shortlyapp.presentation.main

import androidx.lifecycle.viewModelScope
import tk.atna.shortlyapp.R
import tk.atna.shortlyapp.domain.interactor.UrlsInteractor
import tk.atna.shortlyapp.domain.model.ShortenedUrl
import tk.atna.shortlyapp.extension.asStateFlow
import tk.atna.shortlyapp.presentation.base.BaseViewModel
import tk.atna.shortlyapp.presentation.model.InputException
import tk.atna.shortlyapp.stuff.SingleEventFlow

class MainViewModel(
    private val urlsInteractor: UrlsInteractor
) : BaseViewModel() {

    val history = urlsInteractor.getShortenedUrls().asStateFlow(viewModelScope, listOf())
    val submitSuccess = SingleEventFlow<Unit>()

    fun copyUrl(shortenedUrl: ShortenedUrl) {
        // todo: cache
    }

    fun deleteUrl(shortenedUrl: ShortenedUrl) {
        launchJob {
            urlsInteractor.deleteShortenedUrl(shortenedUrl)
        }
    }

    fun shortenUrl(url: String) {
        launchJob {
            if (url.isEmpty()) {
                throw InputException(R.string.add_plate_error)
            } else {
                urlsInteractor.shortenUrl(url)
                submitSuccess.setValue(Unit)
            }
        }
    }
}