package tk.atna.shortlyapp.util

import com.kaspersky.kaspresso.testcases.core.testcontext.BaseTestContext
import tk.atna.shortlyapp.di.DATABASE_NAME

fun BaseTestContext.clearData() {
    device.targetContext.deleteDatabase(DATABASE_NAME)
}