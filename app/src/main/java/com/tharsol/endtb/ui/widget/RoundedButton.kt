package com.tharsol.endtb.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.StateSet
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.tharsol.endtb.R

class RoundedButton : AppCompatTextView {
    @ColorInt
    private var fillColor = 0

    @ColorInt
    private var strokeColor = 0

    @Dimension
    private var strokeWidth = 0

    @ColorInt
    private var pressedColor = 0

    @ColorInt
    private var disabledColor = 0

    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr, 0)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.RoundedButton,
                defStyleAttr,
                defStyleRes
            )
            strokeColor =
                typedArray.getColor(R.styleable.RoundedButton_rb_stroke_color, INVALID_COLOR)
            strokeWidth = typedArray.getDimensionPixelSize(
                R.styleable.RoundedButton_rb_stroke_width,
                getResources().getDimensionPixelSize(R.dimen.rb_default_stroke_width)
            )

            // use the default fill color if stroke isn't specified
            val defaultFillColorRes =
                if (strokeColor == INVALID_COLOR) R.color.rb_default_fill_color else android.R.color.transparent
            val defaultFillColor: Int = ContextCompat.getColor(context, defaultFillColorRes)
            fillColor =
                typedArray.getColor(R.styleable.RoundedButton_rb_fill_color, defaultFillColor)
            disabledColor = typedArray.getColor(
                R.styleable.RoundedButton_rb_disabled_color,
                ContextCompat.getColor(getContext(), R.color.rb_default_disabled_color)
            )
            pressedColor =
                typedArray.getColor(R.styleable.RoundedButton_rb_pressed_color, INVALID_COLOR)
            typedArray.recycle()
        }
        if (attrs == null || attrs.getAttributeValue(ANDROID_NAMESPACE, "gravity") == null) {
            // default to center gravity if it's not specified
            setGravity(Gravity.CENTER)
        }
        updateBackground()
    }

    fun setFillColor(@ColorInt color: Int) {
        fillColor = color
        updateBackground()
    }

    fun setStroke(@Dimension strokeWidth: Int, @ColorInt color: Int) {
        this.strokeWidth = strokeWidth
        strokeColor = color
        updateBackground()
    }

    fun setPressedColor(@ColorInt color: Int) {
        pressedColor = color
        updateBackground()
    }

    fun setDisabledColor(@ColorInt color: Int) {
        disabledColor = color
        updateBackground()
    }

    private fun updateBackground() {
        val stateListDrawable = StateListDrawable()
        val defaultDrawable = createRoundedRectangleDrawable(fillColor, strokeColor, strokeWidth)
        val darkenedFill =
            if (pressedColor == INVALID_COLOR) darkenColor(fillColor) else pressedColor
        val pressedDrawable = createRoundedRectangleDrawable(darkenedFill, strokeColor, strokeWidth)
        val disabledDrawable =
            createRoundedRectangleDrawable(disabledColor, strokeColor, strokeWidth)
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        stateListDrawable.addState(StateSet.WILD_CARD, defaultDrawable)
        background = stateListDrawable
    }

    private fun createRoundedRectangleDrawable(
        fillColor: Int,
        strokeColor: Int,
        strokeWidth: Int
    ): Drawable {
        val rectDrawable = GradientDrawable()
        rectDrawable.shape = GradientDrawable.RECTANGLE
        if (strokeColor != INVALID_COLOR) {
            rectDrawable.setStroke(strokeWidth, strokeColor)
        }
        rectDrawable.setColor(fillColor)
        // using some really large radius here, Android will automatically clip it down to height/2
        rectDrawable.cornerRadius = 10000f
        return rectDrawable
    }

    // Create a slightly darkened color for touch indicator
    private fun darkenColor(originalColor: Int): Int {
        if (originalColor == Color.TRANSPARENT) {
            return ContextCompat.getColor(getContext(), R.color.rb_darken_color)
        }
        val hsv = FloatArray(3)
        Color.colorToHSV(originalColor, hsv)
        hsv[2] *= 0.9f
        return Color.HSVToColor(hsv)
    }

    companion object {
        private const val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"
        private const val INVALID_COLOR = -1
    }
}