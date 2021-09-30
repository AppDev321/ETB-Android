//package com.tharsol.endtb.util
//
//import android.content.Context
//import android.graphics.Typeface
//import android.text.Spannable
//import android.text.SpannableString
//import android.view.MenuItem
//import android.widget.*
//import androidx.collection.LruCache
//import androidx.core.content.res.ResourcesCompat
//import com.tharsol.endtb.R
//
//object Fonts {
//    var FONT_MAGNETO_BOLD = "MAGNETOB.TTF"
//    var FONT_OPEN_SANS_REGULAR = "OpenSans-Regular.ttf"
//    var FONT_OPEN_SANS_LIGHT = "OpenSans-Light.ttf"
//    var FONT_OPEN_SANS_BOLD = "OpenSans-Bold.ttf"
//    var FONT_DIGITAL_7 = "digital-7.ttf"
//    var FONT_DIGITAL_7_BOLD = "DS-DIGIB.TTF"
//    var FONT_MISTRAL_REGULAR = "mistral.ttf"
//    var FONT_GEORGIA_REGULAR = "georgia.ttf"
//    var FONT_GEORGIA_BOLD = "georgiab.ttf"
//    var FONT_FELIX_TITLING_REGULAR = "FELIXTI.TTF"
//    private val sTypefaceCache = LruCache<String, Typeface?>(12)
//    fun getTypeface(context: Context, typefaceName: String): Typeface? {
//        var typeface = sTypefaceCache[typefaceName]
//        if (typeface == null) {
//            typeface =
//                Typeface.createFromAsset(context.assets, String.format("Fonts/%s", typefaceName))
//            sTypefaceCache.put(typefaceName, typeface)
//        }
//        return typeface
//    }
//
//    fun setFontOpenSansRegular(context: Context?, menuItem: MenuItem) {
//        val font = ResourcesCompat.getFont(context!!, R.font.open_sans_regular)
//        val mNewTitle = SpannableString(menuItem.title)
//        mNewTitle.setSpan(
//            CustomTypefaceSpan(context, font!!),
//            0,
//            mNewTitle.length,
//            Spannable.SPAN_INCLUSIVE_INCLUSIVE
//        )
//        menuItem.title = mNewTitle
//    }
//
//    fun setFontMagnetoBold(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_MAGNETO_BOLD)
//    }
//
//    fun setFontFelixTitlingRegular(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_FELIX_TITLING_REGULAR)
//    }
//
//    fun setFontMistralRegular(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_MISTRAL_REGULAR)
//    }
//
//    fun setFontGeorgiaRegular(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_GEORGIA_REGULAR)
//    }
//
//    fun setFontGeorgiaBold(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_GEORGIA_BOLD)
//    }
//
//    fun setFontOpenSansRegular(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_OPEN_SANS_REGULAR)
//    }
//
//    fun setFontOpenSansRegular(editText: EditText) {
//        editText.typeface = getTypeface(editText.context, FONT_OPEN_SANS_REGULAR)
//    }
//
//    fun setFontOpenSansRegular(button: Button) {
//        button.typeface = getTypeface(button.context, FONT_OPEN_SANS_REGULAR)
//    }
//
//    fun setFontOpenSansRegular(checkBox: CheckBox) {
//        checkBox.typeface = getTypeface(checkBox.context, FONT_OPEN_SANS_REGULAR)
//    }
//
//    fun setFontOpenSansRegular(radioButton: RadioButton) {
//        radioButton.typeface = getTypeface(radioButton.context, FONT_OPEN_SANS_REGULAR)
//    }
//
//    fun setFontOpenSansLight(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_OPEN_SANS_LIGHT)
//    }
//
//    fun setFontOpenSansLight(editText: EditText) {
//        editText.typeface = getTypeface(editText.context, FONT_OPEN_SANS_LIGHT)
//    }
//
//    fun setFontOpenSansLight(button: Button) {
//        button.typeface = getTypeface(button.context, FONT_OPEN_SANS_LIGHT)
//    }
//
//    fun setFontOpenSansLight(checkBox: CheckBox) {
//        checkBox.typeface = getTypeface(checkBox.context, FONT_OPEN_SANS_LIGHT)
//    }
//
//    fun setFontOpenSansLight(radioButton: RadioButton) {
//        radioButton.typeface = getTypeface(radioButton.context, FONT_OPEN_SANS_LIGHT)
//    }
//
//    fun setFontOpenSansBold(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_OPEN_SANS_BOLD)
//    }
//
//    fun setFontOpenSansBold(editText: EditText) {
//        editText.typeface = getTypeface(editText.context, FONT_OPEN_SANS_BOLD)
//    }
//
//    fun setFontOpenSansBold(button: Button) {
//        button.typeface = getTypeface(button.context, FONT_OPEN_SANS_BOLD)
//    }
//
//    fun setFontOpenSansBold(checkBox: CheckBox) {
//        checkBox.typeface = getTypeface(checkBox.context, FONT_OPEN_SANS_BOLD)
//    }
//
//    fun setFontOpenSansBold(radioButton: RadioButton) {
//        radioButton.typeface = getTypeface(radioButton.context, FONT_OPEN_SANS_BOLD)
//    }
//
//    fun setFontDigital7Regular(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_DIGITAL_7)
//    }
//
//    fun setFontDigital7Bold(textView: TextView) {
//        textView.typeface = getTypeface(textView.context, FONT_DIGITAL_7)
//    }
//}