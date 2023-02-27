package tk.atna.shortlyapp.presentation.main

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert
import org.junit.Test
import tk.atna.shortlyapp.common.BaseViewModelTest
import tk.atna.shortlyapp.domain.interactor.UrlsInteractor
import tk.atna.shortlyapp.domain.model.ShortenedUrl
import tk.atna.shortlyapp.presentation.model.InputException
import tk.atna.shortlyapp.presentation.model.ShortenedUrlItem

@ExperimentalCoroutinesApi
class MainViewModelTest : BaseViewModelTest() {

    private val urlsInteractor = mockk<UrlsInteractor>(relaxed = true)

    private val shortenedUrlsFlow = MutableSharedFlow<List<ShortenedUrl>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    ).also { it.tryEmit(listOf()) }

    override val viewModel by lazy {
        // this is called during view model creation and therefore is needed to be defined before
        every { urlsInteractor.getShortenedUrls() }.answers { shortenedUrlsFlow }

        MainViewModel(urlsInteractor)
    }

    @Test
    fun `viewmodel init sets initial values`() = runViewModelTest {
        // arrange
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.history.collect()
        }

        // act
        runCurrent()

        // assert
        Assert.assertEquals(listOf<List<ShortenedUrlItem>>(), viewModel.history.value)
        collectJob.cancel()
    }

    @Test
    fun `viewmodel init loads shortened urls and sets history items`() = runViewModelTest {
        // arrange
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.history.collect()
        }

        // act
        shortenedUrlsFlow.tryEmit(listOf(FakeShortenedUrl.url1, FakeShortenedUrl.url2))
        advanceUntilIdle()

        // assert
        verify(exactly = 1) { urlsInteractor.getShortenedUrls() }
        val expected = listOf(FakeShortenedUrl.item1, FakeShortenedUrl.item2)
        Assert.assertEquals(expected, viewModel.history.value)
        collectJob.cancel()
    }

    @Test
    fun `calling copyUrl() triggers history items update with passed url`() = runViewModelTest {
        // arrange
        shortenedUrlsFlow.tryEmit(listOf(FakeShortenedUrl.url2, FakeShortenedUrl.url1))
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.history.collect()
        }

        // act
        advanceUntilIdle()
        viewModel.copyUrl(FakeShortenedUrl.url2)
        runCurrent()

        // assert
        val expected = listOf(
            FakeShortenedUrl.item2.copy(copiedLink = FakeShortenedUrl.url2.shortLink),
            FakeShortenedUrl.item1.copy(copiedLink = FakeShortenedUrl.url2.shortLink)
        )
        Assert.assertEquals(expected, viewModel.history.value)
        collectJob.cancel()
    }

    @Test
    fun `calling copyUrl() triggers history items update with no copied url after timeout`() = runViewModelTest {
        // arrange
        shortenedUrlsFlow.tryEmit(listOf(FakeShortenedUrl.url1, FakeShortenedUrl.url2))
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.history.collect()
        }

        // act
        advanceUntilIdle()
        viewModel.copyUrl(FakeShortenedUrl.url2)
        advanceTimeBy(MainViewModel.DEFAULT_COPY_TIMEOUT)
        runCurrent()

        // assert
        val expected = listOf(FakeShortenedUrl.item1, FakeShortenedUrl.item2)
        Assert.assertEquals(expected, viewModel.history.value)
        collectJob.cancel()
    }

    @Test
    fun `calling deleteUrl() triggers shortened ulr deletion`() = runViewModelTest {
        // act
        viewModel.deleteUrl(FakeShortenedUrl.url2)
        runCurrent()

        // assert
        coVerify(exactly = 1) { urlsInteractor.deleteShortenedUrl(FakeShortenedUrl.url2) }
    }

    @Test
    fun `calling shortenUrl() triggers url shortening and fires success flag`() = runViewModelTest {
        // act
        viewModel.shortenUrl("some_url")
        runCurrent()

        // assert
        coVerify(exactly = 1) { urlsInteractor.shortenUrl("some_url") }
        Assert.assertEquals(Unit, viewModel.submitSuccess.first())
    }

    @Test
    fun `calling shortenUrl() not triggers url shortening and not fires success flag for empty url`() =
        runViewModelTest {
            // arrange
            val values = mutableListOf<Unit>()
            val valuesJob = launch(UnconfinedTestDispatcher()) {
                viewModel.submitSuccess.toList(values)
            }

            // act
            viewModel.shortenUrl("")
            runCurrent()
            valuesJob.cancel()

            // assert
            coVerify(inverse = true) { urlsInteractor.shortenUrl(any()) }
            assert(values.isEmpty()) { "Expected empty list, but was of size ${values.size}" }
        }

    @Test
    fun `calling shortenUrl() triggers special exception throwing for empty url`() =
        runViewModelTest {
            // act
            viewModel.shortenUrl("")
            runCurrent()

            // assert
            assert(viewModel.error.first() is InputException)
        }
}