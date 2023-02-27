package tk.atna.shortlyapp.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import tk.atna.shortlyapp.stuff.DispatcherProvider

@ExperimentalCoroutinesApi
class CoroutineRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
        DispatcherProvider.Default = testDispatcher
        DispatcherProvider.IO = testDispatcher
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
        DispatcherProvider.reset()
    }
}