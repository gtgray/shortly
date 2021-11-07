package tk.atna.shortlyapp.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tk.atna.shortlyapp.stuff.SingleEventFlow

open class BaseViewModel : ViewModel() {

    val loading = MutableStateFlow(false)
    val error = SingleEventFlow<Throwable?>()

    private fun createExceptionHandler(
        error: SingleEventFlow<Throwable?>?,
        exceptionBlock: ((Throwable) -> Unit)? = null
    ): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, exception ->
            exceptionBlock?.invoke(exception)
            error?.setValue(exception)
        }
    }

    protected fun launchJob(
        loading: MutableStateFlow<Boolean>? = this.loading,
        error: SingleEventFlow<Throwable?>? = this.error,
        exceptionBlock: ((Throwable) -> Unit)? = null,
        block: suspend () -> Unit
    ): Job {
        return viewModelScope.launch(createExceptionHandler(error, exceptionBlock)) {
            try {
                error?.setValue(null)
                loading?.value = true
                block.invoke()
            } finally {
                loading?.value = false
            }
        }
    }
}