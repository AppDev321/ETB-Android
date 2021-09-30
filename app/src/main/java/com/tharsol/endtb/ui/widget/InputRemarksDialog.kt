package com.tharsol.endtb.ui.widget

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.LayoutDialogRemarkBinding

class InputRemarksDialog : Dialog {

    var binding: LayoutDialogRemarkBinding? = null
    private var okListener: OKListener? = null
    var isOptional: Boolean
    private var mTitle: String?
    private var mHint: String? = null

    constructor(@NonNull context: Context, @Nullable title: String?, isOptional: Boolean) : super(
        context /*,R.style.AppDialog_Dialog_Full*/
    ) {
        mTitle = title
        this.isOptional = isOptional
        init()
    }

    constructor(
        @NonNull context: Context,
        @Nullable title: String?,
        hint: String?,
        isOptional: Boolean
    ) : super(context /*,R.style.AppDialog_Dialog_Full*/) {
        mTitle = title
        mHint = hint
        this.isOptional = isOptional
        init()
    }

    private fun init() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = LayoutDialogRemarkBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)
        if (getWindow() != null) getWindow()?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        setCanceledOnTouchOutside(false)
        initDisplayAllowances()
    }

    private fun initDisplayAllowances() {

        if (mTitle != null) binding?.textViewHeading?.text = mTitle
        binding?.buttonProceed?.setOnClickListener { onViewClicked() }
    }

    private fun validate(): Boolean {
        if (isOptional) return true
        if (TextUtils.isEmpty(binding?.editMobilNumber?.text)) {
            binding?.editMobilNumber?.error = context.getString(R.string.error_empty_field)
            return false
        }
        return true
    }

    fun onViewClicked() {
        if (validate() && okListener != null) okListener!!.onClick(
            this,
            binding?.editMobilNumber?.text.toString()
        )
    }

    fun setOkListener(okListener: OKListener?) {
        this.okListener = okListener
    }

    interface OKListener {
        fun onClick(dialog: Dialog?, remarks: String?)
    }
}