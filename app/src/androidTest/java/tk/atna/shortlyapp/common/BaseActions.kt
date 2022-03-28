package tk.atna.shortlyapp.common

import io.github.kakaocup.kakao.common.actions.BaseActions
import tk.atna.shortlyapp.view.SingleTapAction

// For more details on using custom click see:
// https://avito-tech.github.io/avito-android/test_framework/Internals/
fun BaseActions.singleTap() {
    view.perform(SingleTapAction())
}