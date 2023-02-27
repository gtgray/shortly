package tk.atna.shortlyapp.util

import io.mockk.clearAllMocks
import org.junit.rules.TestWatcher
import org.junit.runner.Description

object MockkClearRule : TestWatcher() {

    override fun finished(description: Description) {
        clearAllMocks()
    }
}