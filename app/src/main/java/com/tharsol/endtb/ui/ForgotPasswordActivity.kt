package com.tharsol.endtb.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ActivityForgotBinding
import com.tharsol.endtb.extenstions.isValid
import com.tharsol.endtb.network.ApiAdapter
import com.tharsol.endtb.serialize.RequestForgot
import com.tharsol.endtb.util.ActivityUtils
import com.tharsol.endtb.util.ProgressDialogUtils
import com.tharsol.endtb.util.Utilities
import kotlinx.coroutines.launch

class ForgotPasswordActivity : BaseActivity() {

    var binding: ActivityForgotBinding? = null
    var progressDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//        updateTitle(binding?.include?.topBarTitle, getString(R.string.login_title))
        updateHome(binding?.include?.btnUp, true)
        onClickListeners()
    }

    override fun onUp() {
        ActivityUtils.startLoginActivity(this)
        finish()
    }

    private fun onClickListeners() {

        binding?.btnGo?.setOnClickListener {
            onDataSubmit()
        }
    }

    override fun onBackPressed() {
        onUp()
    }

    private fun onDataSubmit() {
        if (TextUtils.isEmpty(binding?.editTextUserName?.text)) {
            binding?.editTextUserName?.setError(getString(R.string.error_empty_field))
            return
        }
        binding?.editTextUserName?.setError(null)
        loadData()

    }

    private fun loadData() {
        lifecycleScope.launch {
            progressDialog =
                ProgressDialogUtils.createProgressDialog(this@ForgotPasswordActivity)
            progressDialog?.show()
            try {
                val request = RequestForgot(
                    binding?.editTextUserName?.text?.toString(),
                    Utilities.getDeviceUdId(this@ForgotPasswordActivity)
                )
                val response = ApiAdapter.apiClient.reset(request)

                if (response.isValid()) {
                    val innerResponse = response.body()
                    if (innerResponse?.successFlag!!) {
                        ProgressDialogUtils.showSuccess(
                            progressDialog!!,
                            getString(R.string.app_name),
                            innerResponse.activityInfo
                        )
                    } else {
                        ProgressDialogUtils.showError(
                            progressDialog!!,
                            getString(R.string.oops),
                            innerResponse.activityInfo
                        )
//                        Utilities.showToast(this@ForgotPasswordActivity, innerResponse.activityInfo)
                    }
                } else {
                    // Show API error.
                    ProgressDialogUtils.showError(
                        progressDialog!!,
                        getString(R.string.oops),
                        response.message()
                    )
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                progressDialog?.dismiss()
            }

        }
    }
}