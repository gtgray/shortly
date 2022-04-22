package tk.atna.shortlyapp.view

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import tk.atna.shortlyapp.common.canHandleClick

class CanHandleClickMatcher : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description) {
        description.appendText("is clickable itself or by parent")
    }

    public override fun matchesSafely(view: View): Boolean {
        return view.canHandleClick()
    }
}