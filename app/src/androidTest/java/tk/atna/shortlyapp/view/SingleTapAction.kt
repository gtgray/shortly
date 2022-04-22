package tk.atna.shortlyapp.view

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.PrecisionDescriber
import androidx.test.espresso.action.Press
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import tk.atna.shortlyapp.util.VisibleCenterGlobalCoordinatesProvider

class SingleTapAction(
    private val coordinatesProvider: CoordinatesProvider = VisibleCenterGlobalCoordinatesProvider(),
    private val precisionDescriber: PrecisionDescriber = Press.FINGER
) : ViewAction {

    override fun getDescription(): String = "single click (aka tap)"

    override fun getConstraints(): Matcher<View> = Matchers.allOf(
        ViewMatchers.isEnabled(),
        CanHandleClickMatcher()
    )

    override fun perform(uiController: UiController, view: View) {
        assert(view.callOnClick()) { "Can't do the click on $view" }
//        val rootView = view.rootView
//
//        val downEvent = coordinatesProvider.calculateCoordinates(view)
//            .obtainDownEvent(precisionDescriber.describePrecision())
//        rootView.dispatchTouchEvent(downEvent)
//
//        uiController.loopMainThreadForAtLeast(ViewConfiguration.getTapTimeout().toLong())
//
//        val upEvent = downEvent.obtainUpEvent()
//        rootView.dispatchTouchEvent(upEvent)
//
//        downEvent.recycle()
//        upEvent.recycle()
//
//        uiController.loopMainThreadForAtLeast(ViewConfiguration.getPressedStateDuration().toLong())
    }
}