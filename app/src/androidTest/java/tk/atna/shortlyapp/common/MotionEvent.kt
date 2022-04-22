package tk.atna.shortlyapp.common

import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent

private const val NORMAL_PRESSURE = 1f
private const val NORMAL_SIZE = 1f
private const val WITHOUT_MODIFIERS_META_STATE = 0

fun FloatArray.obtainDownEvent(precision: FloatArray): MotionEvent = MotionEvent.obtain(
    SystemClock.uptimeMillis(),
    SystemClock.uptimeMillis(),
    MotionEvent.ACTION_DOWN,
    this[0],
    this[1],
    NORMAL_PRESSURE,
    NORMAL_SIZE,
    WITHOUT_MODIFIERS_META_STATE,
    precision[0],
    precision[1],
    InputDevice.SOURCE_UNKNOWN,
    0
)

fun MotionEvent.obtainUpEvent(): MotionEvent = MotionEvent.obtain(
    downTime,
    SystemClock.uptimeMillis(),
    MotionEvent.ACTION_UP,
    x,
    y,
    pressure,
    size,
    metaState,
    xPrecision,
    yPrecision,
    deviceId,
    edgeFlags
)