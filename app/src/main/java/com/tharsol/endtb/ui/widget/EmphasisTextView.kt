package com.tharsol.endtb.ui.widget

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.tharsol.endtb.util.Emphasis
import com.tharsol.endtb.util.HighlightMode
import java.util.regex.Pattern

class EmphasisTextView : AppCompatTextView, Emphasis {
    private var mHighlightColor = Color.YELLOW
    private var mTextToHighlight: String? = null
    private var mCaseInsensitive = true
    private var mHighlightMode: HighlightMode = HighlightMode.BACKGROUND

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    override fun setTextHighlightColor(highlightColorHex: String) {
        mHighlightColor = Color.parseColor(highlightColorHex)
    }

    override fun setTextHighlightColor(colorResource: Int) {
        mHighlightColor = resources.getColor(colorResource)
    }

    override fun setHighlightMode(highlightMode: HighlightMode) {
        mHighlightMode = highlightMode
    }

    override fun setTextToHighlight(textToHighlight: String) {
        mTextToHighlight = textToHighlight
        highlight()
    }

    override fun setCaseInsensitive(caseInsensitive: Boolean) {
        mCaseInsensitive = caseInsensitive
    }

    fun setText(text: String?, textToHighlight: String?) {
        setText(text)
        mTextToHighlight = textToHighlight
        highlight()
    }

    override fun highlight() {
        if (TextUtils.isEmpty(mTextToHighlight) || TextUtils.isEmpty(text)) {
            return
        }
        val text = text.toString()
        val spannableString: Spannable = SpannableString(text)
        val pattern: Pattern = if (mCaseInsensitive) {
            Pattern.compile(
                Pattern.quote(mTextToHighlight!!),
                Pattern.CASE_INSENSITIVE
            )
        } else {
            Pattern.compile(Pattern.quote(mTextToHighlight!!))
        }
        val matcher = pattern.matcher(text)
        while (matcher.find()) {
            val characterStyle: CharacterStyle = if (mHighlightMode === HighlightMode.BACKGROUND) {
                BackgroundColorSpan(mHighlightColor)
            } else {
                ForegroundColorSpan(mHighlightColor)
            }
            spannableString.setSpan(
                characterStyle,
                matcher.start(),
                matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        setText(spannableString)
    }
}