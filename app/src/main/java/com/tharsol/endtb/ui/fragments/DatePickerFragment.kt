package com.tharsol.endtb.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    var mOnDateSetByUserListener: OnDateSetByUserListener? = null
    var mOnCreateDialogListener: OnCreateDialogListener? = null
    fun setOnCreateDialogListener(listener: OnCreateDialogListener?) {
        mOnCreateDialogListener = listener
    }

    fun setOnDateSetByUserListener(listener: OnDateSetByUserListener?) {
        mOnDateSetByUserListener = listener
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mOnCreateDialogListener != null) {
            return mOnCreateDialogListener!!.onCreateDialog(savedInstanceState)
        }
        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(requireContext(), this, year, month, day)
        val maxTime = maxTime
        if (maxTime != -1L) dialog.getDatePicker().setMaxDate(maxTime)
        return dialog
    }

    private val maxTime: Long
        private get() = if (getArguments() == null) -1 else getArguments()!!.getLong(
            KEY_MAX_TIME,
            -1
        )

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        if (mOnDateSetByUserListener != null) {
            mOnDateSetByUserListener!!.onDateSetByUser(view, year, month, day)
        }
    }

    interface OnCreateDialogListener {
        fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    }

    interface OnDateSetByUserListener {
        fun onDateSetByUser(view: DatePicker?, year: Int, month: Int, day: Int)
    }

    companion object {
        const val KEY_MAX_TIME = "kMaxTime"
    }
}