package tk.atna.shortlyapp.common

import android.view.View
import android.view.ViewGroup

fun View?.canHandleClick(): Boolean {
    if (this == null) return false
    val parent = this.parent as? ViewGroup
    return when {
        isClickable -> true
        else -> parent.canHandleClick()
    }
}