package com.tharsol.endtb.util

import android.content.Context
import android.widget.EditText
import com.tharsol.endtb.R

object ValidationUtils
{

    fun validatePassword(view: EditText?): Boolean
    {
        return view?.text?.length ?: 0 > 3
    }

    fun validateMobile(view: EditText?): Boolean
    {
        return view?.text?.length?.compareTo(8) == 1
    }

    fun validateName(view: EditText?): Boolean
    {
        return view?.text?.length?.compareTo(3) == 1
    }

    fun validateAge(view: EditText?): Boolean
    {
        if (view?.text!!.isEmpty()) return false
        return view.text!!.isNotEmpty() && view.text?.toString()?.toInt()?.compareTo(0) == 1
    }

    fun validateCNIC(view: EditText?): Boolean
    {
        return view?.text!!.isEmpty() || (view.text?.toString()?.toLong() ?: 0 != 13L)
    }

    fun validateInternet(context: Context): Boolean
    {
        if (!Utilities.isInternetConnected(App.context))
        {
            Utilities.showToast(context, context.getString(R.string.error_msg_internet))
            return false
        }
        return true
    }

}