package tk.atna.shortlyapp.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val DEFAULT_STOP_TIMEOUT = 5000L

fun <T> Flow<T>.asStateFlow(scope: CoroutineScope, initialValue: T) = stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(DEFAULT_STOP_TIMEOUT),
    initialValue = initialValue
)

fun <T> Flow<T>.collect(owner: LifecycleOwner, collector: (T) -> Unit) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        collect { collector.invoke(it) }
    }
}