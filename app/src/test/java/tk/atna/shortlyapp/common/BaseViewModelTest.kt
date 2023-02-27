package tk.atna.shortlyapp.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import tk.atna.shortlyapp.presentation.base.BaseViewModel

@ExperimentalCoroutinesApi
abstract class BaseViewModelTest : BaseTest() {

    // CAUTION! Override as LAZY property!!!
    protected abstract val viewModel: BaseViewModel

    protected fun runViewModelTest(
        initBlock: TestScope.() -> Unit = { viewModel },
        testBody: suspend TestScope.() -> Unit
    ) = runTest {
        initBlock.invoke(this)
        testBody.invoke(this)
    }
}