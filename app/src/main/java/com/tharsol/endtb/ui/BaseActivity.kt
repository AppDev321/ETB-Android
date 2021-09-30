package com.tharsol.endtb.ui


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.tharsol.endtb.R
import com.tharsol.endtb.util.OnTitleUpdate
import com.tharsol.endtb.util.Utilities

open class BaseActivity : AppCompatActivity(), OnTitleUpdate
{

    var title: TextView? = null
    var btnHomeUp: View? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //        if (!Utilities.isAllowScreenCapture(this))
        //        {
        //            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //        }
        //        Thread.setDefaultUncaughtExceptionHandler(TopExceptionHandler(this))
        //        window.addFlags(WindowManager.LayoutParams.FLAG_fu)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //            val window = window
        //            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //        }
    }

    private fun updateRoundCorner(toolbar: MaterialToolbar)
    {

        val materialShapeDrawable: MaterialShapeDrawable = toolbar.getBackground() as MaterialShapeDrawable
        materialShapeDrawable.setShapeAppearanceModel(materialShapeDrawable.getShapeAppearanceModel().toBuilder().setBottomRightCorner(CornerFamily.ROUNDED, resources.getDimension(R.dimen.dimen_size_32dp)).build())
    }


    fun updateHome(view: View?, up: Boolean)
    {
        btnHomeUp = view
        btnHomeUp?.let {
            it.setOnClickListener(onUpClick)
            it.visibility = if (up) View.VISIBLE else View.GONE
        }
    }

    private val onUpClick = View.OnClickListener {
        onUp()
    }

    open fun onUp()
    {

    }

    fun updateTitle(view: TextView?, titleText: String?)
    {
        updateTitle(view)
        updateTitle(titleText)
    }

    fun updateTitle(view: TextView?)
    {
        title = view
        title?.visibility = View.VISIBLE
    }

    fun updateTitle(titleText: String?)
    {
        title?.text = titleText
    }

    //    fun setSupportActionBar(toolbar: MaterialToolbar, enableHomeUp: Boolean) {
    //        setSupportActionBar(toolbar)
    //        val actionBar = supportActionBar
    //        if (enableHomeUp && actionBar != null) {
    //            actionBar.setDisplayHomeAsUpEnabled(true)
    //        }
    //    }

    //   override fun setSupportActionBar(toolbar: Toolbar?) {
    //        toolbar?.let {
    //            super.setSupportActionBar(it)
    ////            updateRoundCorner(it)
    //            setTitleFont(this)
    //        }
    //
    //    }

    //    private fun setTitleFont(activity: AppCompatActivity) {
    //        setTitleFont(activity, activity.supportActionBar!!.title)
    //    }
    //
    //    fun setTitleFont(activity: AppCompatActivity, title: CharSequence?) {
    //        val s = SpannableString(title)
    //        s.setSpan(TypefaceSpan(this), 0, title!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    //        activity.supportActionBar!!.setTitle(s)
    //    }
    //
    //    val supportTitle: String?
    //        get() = if (supportActionBar != null
    //            && supportActionBar!!.title != null
    //        ) supportActionBar!!.title.toString() else null
    //
    //    fun setSubTitleFont(activity: AppCompatActivity) {
    //        setSubTitleFont(activity, activity.supportActionBar!!.subtitle)
    //    }
    //
    //    fun setSubTitleFont(activity: AppCompatActivity, subTitle: CharSequence?) {
    //        val s = SpannableString(subTitle)
    //        s.setSpan(TypefaceSpan(this), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    //        activity.supportActionBar!!.setSubtitle(s)
    //    }

    fun hideKeyboard()
    {
        val view = this.currentFocus
        Utilities.hideKeyboard(view)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            R.id.home ->
            {
                onBackPressed()
                true
            }
            else      -> super.onOptionsItemSelected(item)
        }
    }

    override fun startActivity(intent: Intent)
    {
        super.startActivity(intent)
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    fun startActivityForResult(intent: Intent?)
    {
        super.startActivity(intent)
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
    }

    override fun onUpdate(title: String)
    {
        updateTitle(title)
    }

    override fun onUpdateSubTitle(title: String)
    {
        updateTitle(title)
    }

    companion object
    {
        fun showKeyboard(editText: EditText)
        {
            val imm = editText.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }


}