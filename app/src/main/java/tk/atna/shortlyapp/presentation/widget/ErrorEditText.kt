package tk.atna.shortlyapp.presentation.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import tk.atna.shortlyapp.R
import tk.atna.shortlyapp.extension.hideKeyboard

class ErrorEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var cachedHint: CharSequence? = null

    init {
        setBackgroundResource(R.drawable.edittext_bg)
        setTextColor(context.getColor(R.color.grayish_violet))
        setHintTextColor(context.getColor(R.color.edittext_hint_slc))
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) hideError() else hideKeyboard()
    }

    fun showError(message: String? = null) {
        setBackgroundResource(R.drawable.edittext_error_bg)
        setTextColor(context.getColor(R.color.red))
        setHintTextColor(context.getColor(R.color.red))
        // cache initial hint
        if (message != null) {
            if (cachedHint == null) cachedHint = hint
            hint = message
        }
    }

    fun hideError() {
        setBackgroundResource(R.drawable.edittext_bg)
        setTextColor(context.getColor(R.color.grayish_violet))
        setHintTextColor(context.getColor(R.color.edittext_hint_slc))
        // set initial hint back
        if (cachedHint != null) {
            hint = cachedHint
            cachedHint = null
        }
    }
}