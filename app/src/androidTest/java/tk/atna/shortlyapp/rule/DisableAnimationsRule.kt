package tk.atna.shortlyapp.rule

import android.provider.Settings
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test rule for disabling animations before test start.
 * Animations are re-enabled after test finish.
 */
class DisableAnimationsRule : TestRule {

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                disableAnimations()
                try {
                    base.evaluate()
                } finally {
                    enableAnimations()
                }
            }
        }
    }

    internal fun enableAnimations() {
        with(device) {
            executeShellCommand(TRANSITION_ANIMATION_SCALE.format(ENABLED))
            executeShellCommand(WINDOW_ANIMATION_SCALE.format(ENABLED))
            executeShellCommand(ANIMATOR_DURATION_SCALE.format(ENABLED))
        }
    }

    internal fun disableAnimations() {
        with(device) {
            executeShellCommand(TRANSITION_ANIMATION_SCALE.format(DISABLED))
            executeShellCommand(WINDOW_ANIMATION_SCALE.format(DISABLED))
            executeShellCommand(ANIMATOR_DURATION_SCALE.format(DISABLED))
        }
    }

    companion object {
        private const val DISABLED = 0
        private const val ENABLED = 1
        private const val TRANSITION_ANIMATION_SCALE =
            "settings put global ${Settings.Global.TRANSITION_ANIMATION_SCALE} %d"
        private const val WINDOW_ANIMATION_SCALE =
            "settings put global ${Settings.Global.WINDOW_ANIMATION_SCALE} %d"
        private const val ANIMATOR_DURATION_SCALE =
            "settings put global ${Settings.Global.ANIMATOR_DURATION_SCALE} %d"
    }
}