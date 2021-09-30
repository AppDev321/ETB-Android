package com.tharsol.endtb.util

import android.content.Context
import android.graphics.Paint
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import androidx.core.content.res.ResourcesCompat
import com.tharsol.endtb.R

open class TypefaceSpan(private val mContext: Context) : MetricAffectingSpan() {
    override fun updateMeasureState(p: TextPaint) {
        p.typeface = ResourcesCompat.getFont(mContext, R.font.open_sans_regular)
        p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
    }

    override fun updateDrawState(tp: TextPaint) {
        tp.typeface = ResourcesCompat.getFont(mContext, R.font.open_sans_regular)
        tp.flags = tp.flags or Paint.SUBPIXEL_TEXT_FLAG
    }
}