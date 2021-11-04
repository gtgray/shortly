package tk.atna.shortlyapp.presentation.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

open class BaseViewModel : ViewModel(), CoroutineScope by MainScope() {

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}