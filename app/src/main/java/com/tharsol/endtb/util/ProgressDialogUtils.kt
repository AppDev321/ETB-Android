package com.tharsol.endtb.util

import android.content.Context
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tharsol.endtb.R

object ProgressDialogUtils
{
    fun createProgressDialog(context: Context, msg: String? = null): SweetAlertDialog
    {
        val mProgressDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.setCancelable(false)
        mProgressDialog.titleText = msg ?: context.getString(R.string.LOADING)

        mProgressDialog.progressHelper.circleRadius = 80
        mProgressDialog.progressHelper.barColor = ContextCompat.getColor(App.context,
                R.color.colorPrimary)
        return mProgressDialog
    }

    fun showProgress(dialog: SweetAlertDialog,
                     title: String? = null,
                     message: String? = null): SweetAlertDialog
    {
        dialog.titleText = title ?: dialog.context.getString(R.string.LOADING)
        dialog.contentText = message
        dialog.confirmText = dialog.context.getString(R.string.ok)
        dialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
        return dialog
    }

    fun showError(dialog: SweetAlertDialog, title: String?, message: String?)
    {
        dialog.titleText = title
        dialog.contentText = message
        dialog.confirmText = dialog.context.getString(R.string.ok)
        dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE)
    }

    fun showSuccess(dialog: SweetAlertDialog,
                    title: String?,
                    message: String?,
                    okListener: SweetAlertDialog.OnSweetClickListener? = null)
    {
        dialog.titleText = title
        dialog.contentText = message
        dialog.confirmText = dialog.context.getString(R.string.ok)
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.setConfirmClickListener(okListener)
    }


}