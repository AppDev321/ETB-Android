package com.tharsol.endtb.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ActivityChangePasswordBinding
import com.tharsol.endtb.extenstions.isValid
import com.tharsol.endtb.network.ApiAdapter
import com.tharsol.endtb.serialize.RequestChangePassword
import com.tharsol.endtb.util.ProgressDialogUtils
import com.tharsol.endtb.util.Utilities
import com.tharsol.endtb.util.ValidationUtils
import kotlinx.coroutines.launch

class ChangePasswordActivity : BaseActivity() {

    var binding: ActivityChangePasswordBinding? = null
    var progressDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        updateTitle(binding?.include?.topBarTitle, getString(R.string.change_password))
        updateHome(binding?.include?.btnUp, true)
        onClickListeners()

    }

    override fun onUp() {
        onBackPressed()
    }

    private fun onClickListeners() {
        binding?.btnGo?.setOnClickListener {
            onSubmit()
        }
    }


    fun onSubmit() {


        if (!ValidationUtils.validatePassword(binding?.editTextCurrentPassword?.editor)) {
            binding?.editTextCurrentPassword?.setError(getString(R.string.error_password_field))
            return
        }
        binding?.editTextCurrentPassword?.setError(null)

        if (!ValidationUtils.validatePassword(binding?.editTextPassword?.editor)) {
            binding?.editTextPassword?.setError(getString(R.string.error_password_field))
            return
        }
        binding?.editTextPassword?.setError(null)

        if (!TextUtils.equals(
                binding?.editTextRepeatPassword?.text,
                binding?.editTextPassword?.text
            )
        ) {
            binding?.editTextRepeatPassword?.setError(getString(R.string.error_repeat_password_field))
            return
        }
        binding?.editTextRepeatPassword?.setError(null)
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            progressDialog =
                ProgressDialogUtils.createProgressDialog(this@ChangePasswordActivity)
            progressDialog?.show()
            try {
                val request = RequestChangePassword(
                    binding?.editTextCurrentPassword?.text.toString(),
                    binding?.editTextPassword?.text.toString(),
                    Utilities.getDeviceUdId(this@ChangePasswordActivity)
                )
                val response = ApiAdapter.apiClient.changePassword(request)

                if (response.isValid()) {
                    val innerResponse = response.body()
                    Utilities.showToast(this@ChangePasswordActivity, innerResponse?.activityInfo)
                    if (innerResponse?.successFlag!!) {
                        finish()
                    }
                } else {
                    // Show API error.
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            progressDialog?.dismiss()

        }
    }


}