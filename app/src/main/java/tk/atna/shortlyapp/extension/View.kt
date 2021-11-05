package tk.atna.shortlyapp.extension

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.isHitByMotionEvent(event: MotionEvent): Boolean {
    val rect = Rect()
    return getGlobalVisibleRect(rect) && rect.contains(event.rawX.toInt(), event.rawY.toInt())
}

fun View.setKeyboardWindowInsetsListener(action: (visible: Boolean) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        action.invoke(insets.isVisible(WindowInsetsCompat.Type.ime()))
        insets
    }
}