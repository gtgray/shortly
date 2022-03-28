package tk.atna.shortlyapp.scenario

import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import tk.atna.shortlyapp.common.singleTap
import tk.atna.shortlyapp.screen.MainScreen
import tk.atna.shortlyapp.screen.checkAddPlateDisplayed
import tk.atna.shortlyapp.screen.checkEmptyDisplayed
import tk.atna.shortlyapp.screen.checkEnteredUrlShortened
import tk.atna.shortlyapp.screen.checkHistoryDisplayed
import tk.atna.shortlyapp.screen.enterUrl

class ShortenAnUrlScenario : Scenario() {

    override val steps: TestContext<Unit>.() -> Unit = {
        testLogger.i("Shorten an url scenario started")

        step("Launch main screen and proceed with url shortening") {
            MainScreen {
                checkEmptyDisplayed()
                checkAddPlateDisplayed()
                enterUrl()
                shortenBtn { singleTap() }
            }
        }
        step("Check shortened url in the list") {
            MainScreen {
                checkHistoryDisplayed()
                checkEnteredUrlShortened()
                checkAddPlateDisplayed()
            }
        }
        testLogger.i("Shorten an url scenario finished")
    }
}