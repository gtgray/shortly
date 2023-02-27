package tk.atna.shortlyapp.stuff

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DispatcherProvider {

    val Main: CoroutineDispatcher = Dispatchers.Main

    var Default: CoroutineDispatcher = Dispatchers.Default

    var IO: CoroutineDispatcher = Dispatchers.IO

    fun reset() {
        Default = Dispatchers.Default
        IO = Dispatchers.IO
    }
}