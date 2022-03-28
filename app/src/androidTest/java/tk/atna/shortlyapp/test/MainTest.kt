package tk.atna.shortlyapp.test

import androidx.test.core.app.ActivityScenario
import androidx.test.filters.LargeTest
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.params.FlakySafetyParams
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import org.junit.Rule
import org.junit.Test
import tk.atna.shortlyapp.extension.isAppClosed
import tk.atna.shortlyapp.presentation.main.MainActivity
import tk.atna.shortlyapp.rule.DisableAnimationsRule
import tk.atna.shortlyapp.scenario.ShortenAnUrlScenario
import tk.atna.shortlyapp.screen.MainScreen
import tk.atna.shortlyapp.screen.checkAddPlateDisplayed
import tk.atna.shortlyapp.screen.checkEmptyDisplayed
import tk.atna.shortlyapp.screen.checkEmptyUrlErrorDisplayed
import tk.atna.shortlyapp.screen.checkHistoryDisplayed
import tk.atna.shortlyapp.util.clearData

@LargeTest
class MainTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.simple {
        flakySafetyParams = FlakySafetyParams.custom(timeoutMs = 20_000)
    }
) {

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    @Test
    fun noHistoryTest() {
        before {
            clearData()
            ActivityScenario.launch(MainActivity::class.java)
        }.after {
            Unit
        }.run {
            step("Launch main screen, check empty stub and exit") {
                MainScreen {
                    checkEmptyDisplayed()
                    checkAddPlateDisplayed()
                }
            }
            pressBackEndExitApp()
        }
    }

    @Test
    fun shortenAnEmptyUrlTest() {
        before {
            clearData()
            ActivityScenario.launch(MainActivity::class.java)
        }.after {
            Unit
        }.run {
            step("Launch main screen and proceed with empty url shortening") {
                MainScreen {
                    checkEmptyDisplayed()
                    checkAddPlateDisplayed()
                    shortenBtn { click() }
                    checkEmptyUrlErrorDisplayed()
                }
            }
            pressBackEndExitApp()
        }
    }

    @Test
    fun shortenAnUrlTest() {
        before {
            clearData()
            ActivityScenario.launch(MainActivity::class.java)
        }.after {
            Unit
        }.run {
            scenario(ShortenAnUrlScenario())
            pressBackEndExitApp()
        }
    }

    @Test
    fun historyExistsTest() {
        before {
            clearData()
            ActivityScenario.launch(MainActivity::class.java)
        }.after {
            Unit
        }.run {
            scenario(ShortenAnUrlScenario())

            pressBackEndExitApp()
            ActivityScenario.launch(MainActivity::class.java)

            step("Launch main screen, check history and exit") {
                MainScreen {
                    checkHistoryDisplayed()
                    checkAddPlateDisplayed()
                }
            }
            pressBackEndExitApp()
        }
    }

    private fun TestContext<Unit>.pressBackEndExitApp() {
        step("Press back button and exit app") {
            device.exploit.pressBack(false)
            isAppClosed()
        }
    }
}