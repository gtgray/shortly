package tk.atna.shortlyapp.stuff

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SingleEventFlow<T> {

    private val _event = Channel<T>(Channel.CONFLATED)
    private val event = _event.receiveAsFlow()

    fun setValue(event: T) {
        _event.trySend(event)
    }

    fun collect(owner: LifecycleOwner, collector: (T) -> Unit) = owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
            event.collect { collector.invoke(it) }
        }
    }
}