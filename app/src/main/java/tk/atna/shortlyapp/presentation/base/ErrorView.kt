package tk.atna.shortlyapp.presentation.base

import android.content.Context
import tk.atna.shortlyapp.R
import tk.atna.shortlyapp.domain.model.ApiException

abstract class ErrorView(
    protected val context: Context
) {

    fun showApiError(e: ApiException?) = showError(e?.errorMessage ?: context.getString(R.string.error_unknown))

    fun showUnknownError() = showError(context.getString(R.string.error_unknown))

    abstract fun showError(message: String)
}