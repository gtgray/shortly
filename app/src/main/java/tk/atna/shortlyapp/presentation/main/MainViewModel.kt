package tk.atna.shortlyapp.presentation.main

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import tk.atna.shortlyapp.R
import tk.atna.shortlyapp.domain.interactor.UrlsInteractor
import tk.atna.shortlyapp.domain.model.ShortenedUrl
import tk.atna.shortlyapp.extension.asStateFlow
import tk.atna.shortlyapp.presentation.base.BaseViewModel
import tk.atna.shortlyapp.presentation.model.InputException
import tk.atna.shortlyapp.presentation.model.ShortenedUrlItem
import tk.atna.shortlyapp.stuff.SingleEventFlow

class MainViewModel(
    private val urlsInteractor: UrlsInteractor
) : BaseViewModel() {

    private var copiedLink = MutableStateFlow<String?>(null)
    private var copyTimeout: Job? = null
    val history = copiedLink.combine(urlsInteractor.getShortenedUrls()) { copied, urls ->
        urls.map { ShortenedUrlItem(it, copied) }
    }.asStateFlow(viewModelScope, listOf())

    val submitSuccess = SingleEventFlow<Unit>()

    fun copyUrl(shortenedUrl: ShortenedUrl) {
        copiedLink.value = shortenedUrl.shortLink

        copyTimeout?.cancel()
        copyTimeout = launchJob(loading = null) {
            delay(DEFAULT_COPY_TIMEOUT)
            copiedLink.value = null
        }
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

    companion object {
        const val DEFAULT_COPY_TIMEOUT = 5000L
    }
}