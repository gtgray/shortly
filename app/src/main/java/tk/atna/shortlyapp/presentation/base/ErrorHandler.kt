package tk.atna.shortlyapp.presentation.base

import tk.atna.shortlyapp.domain.model.ApiException

open class ErrorHandler constructor(
    protected val errorView: ErrorView
) {

    open fun handle(t: Throwable?) {
        when (t) {
            null -> Unit
            is ApiException -> errorView.showApiError(t)
            else -> errorView.showUnknownError()
        }
    }
}