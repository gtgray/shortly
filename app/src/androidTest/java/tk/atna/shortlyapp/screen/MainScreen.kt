package tk.atna.shortlyapp.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import tk.atna.shortlyapp.R
import tk.atna.shortlyapp.presentation.main.MainFragment

object MainScreen : KScreen<MainScreen>() {

    internal const val TEST_URL = "https://developer.android.com/training/testing/instrumented-tests/ui-tests"

    override val layoutId: Int = R.layout.main_fr
    override val viewClass: Class<*> = MainFragment::class.java

    val emptyView = KView { withId(R.id.vg_empty) }
    val emptyLogo = KImageView {
        withParent { withId(R.id.vg_empty) }
        withId(R.id.iv_logo)
    }
    val emptyImage = KImageView {
        withParent { withId(R.id.vg_empty) }
        withId(R.id.iv_image)
    }
    val emptyTitleText = KTextView {
        withParent { withId(R.id.vg_empty) }
        withId(R.id.tv_title)
    }
    val emptyDescText = KTextView {
        withParent { withId(R.id.vg_empty) }
        withId(R.id.tv_desc)
    }

    val addPlateView = KView { withId(R.id.vg_add_plate) }
    val inputUrlField = KEditText {
        withParent { withId(R.id.vg_add_plate) }
        withId(R.id.et_url)
    }
    val shortenBtn = KButton {
        withParent { withId(R.id.vg_add_plate) }
        withId(R.id.btn_shorten)
    }

    val historyView = KView { withId(R.id.vg_history) }
    val historyTitleText = KTextView {
        isDescendantOfA { withId(R.id.vg_history) }
        withId(R.id.tv_title)
    }
    val historyList = KRecyclerView({ withId(R.id.rv_history) }, { itemType(::ShortenedUrlItem) })

    internal class ShortenedUrlItem(parent: Matcher<View>) : KRecyclerItem<ShortenedUrlItem>(parent) {
        val originalText = KTextView(parent) { withId(R.id.tv_original) }
        val shortenedText = KTextView(parent) { withId(R.id.tv_shortened) }
        val copyBtn = KButton(parent) { withId(R.id.btn_copy) }
        val deleteBtn = KButton(parent) { withId(R.id.btn_delete) }
    }
}

fun MainScreen.checkEmptyDisplayed() {
    historyView {
        isGone()
    }
    emptyView {
        isDisplayed()
    }
    emptyLogo {
        isDisplayed()
        hasDrawable(R.drawable.svg_logo)
    }
    emptyImage {
        isDisplayed()
// todo: not work. probably, for reason of scaling.
//       try to apply ((Drawable) -> Bitmap) transformation to bring both images to the same dimensions
//        hasDrawable(R.drawable.svg_empty_image)
    }
    emptyTitleText {
        isDisplayed()
        hasText(R.string.empty_title)
    }
    emptyDescText {
        isDisplayed()
        hasText(R.string.empty_desc)
    }
}

fun MainScreen.checkHistoryDisplayed() {
    emptyView {
        isGone()
    }
    historyView {
        isDisplayed()
    }
    historyTitleText {
        isDisplayed()
        hasText(R.string.history_title)
    }
    historyList {
        isDisplayed()
    }
}

fun MainScreen.checkAddPlateDisplayed() {
    addPlateView {
        isDisplayed()
    }
    inputUrlField {
        isDisplayed()
        hasHint(R.string.add_plate_hint)
        hasEmptyText()
    }
    shortenBtn {
        isDisplayed()
        hasText(R.string.add_plate_btn)
    }
}

fun MainScreen.checkEmptyUrlErrorDisplayed() {
    inputUrlField {
        hasHint(R.string.add_plate_error)
        hasTextColor(R.color.red)
    }
}

fun MainScreen.enterUrl() {
    inputUrlField {
        replaceText(this@enterUrl.TEST_URL)
    }
}

fun MainScreen.checkEnteredUrlShortened() {
    historyList {
        childWith<MainScreen.ShortenedUrlItem> {
            withDescendant {
                withId(R.id.tv_original)
                withText(this@checkEnteredUrlShortened.TEST_URL)
            }
        }.perform {
            originalText {
                isDisplayed()
                hasText(this@checkEnteredUrlShortened.TEST_URL)
            }
            shortenedText {
                isDisplayed()
                hasAnyText()
            }
        }
    }
}