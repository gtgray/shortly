package tk.atna.shortlyapp.extension

import android.app.ActivityManager

fun isAppClosed(): Boolean {
    val appProcessInfo = ActivityManager.RunningAppProcessInfo()
    ActivityManager.getMyMemoryState(appProcessInfo)
    return appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE
}