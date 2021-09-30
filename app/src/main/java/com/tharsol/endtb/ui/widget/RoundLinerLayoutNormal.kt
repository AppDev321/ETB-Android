package com.tharsol.endtb.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.tharsol.endtb.R
import com.tharsol.endtb.util.ViewUtils

class RoundLinerLayoutNormal : LinearLayout {
    constructor(context: Context?) : super(context) {
        initBackground()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        initBackground()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initBackground()
    }

    private fun initBackground() {
        setBackground(
            ViewUtils.generateBackgroundWithShadow(
                this, R.color.white,
                R.dimen.radius_corner, R.color.redShadow, R.dimen.elevation, Gravity.BOTTOM
            )
        )
    }
}