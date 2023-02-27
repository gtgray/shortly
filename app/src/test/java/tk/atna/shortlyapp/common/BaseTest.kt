package tk.atna.shortlyapp.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import tk.atna.shortlyapp.util.CoroutineRule
import tk.atna.shortlyapp.util.MockkClearRule

@ExperimentalCoroutinesApi
abstract class BaseTest {

    @get:Rule
    val mockkClearRule = MockkClearRule

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()
}