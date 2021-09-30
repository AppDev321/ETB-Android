package com.tharsol.endtb.extenstions

import android.text.InputFilter
import android.widget.EditText
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.tharsol.endtb.R
import com.tharsol.endtb.ui.widget.CustomEditText
import com.tharsol.endtb.ui.widget.RoundedFieldView
import retrofit2.Response

fun <T> Response<T>.isValid(): Boolean {
    return this.isSuccessful && body() != null
}

fun NavigationView.changeCornerRadius() {
    val navViewBackground: MaterialShapeDrawable = background as MaterialShapeDrawable
    val radius = resources.getDimension(R.dimen.nav_header_radius)
    navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
        .toBuilder()
        .setTopLeftCorner(CornerFamily.ROUNDED, radius)
        .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
        .build()
}


fun RoundedFieldView.getFullNumber(): String {
    return if (prefix == null) text.toString() else prefix!!.plus(text.toString())
}

fun RoundedFieldView.getNumberWithoutSpace(): String {
    return getFullNumber().replace(" ", "")
}

fun CustomEditText.getFullNumber(): String {
    return if (prefix == null) text.toString() else prefix!!.plus(text.toString())
}

fun CustomEditText.getNumberWithoutSpace(): String {
    return getFullNumber().replace(" ", "")
}

fun EditText.limitLength(maxLength: Int) {
    filters = arrayOf(InputFilter.LengthFilter(maxLength))
}