package com.tharsol.endtb.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup.OnPositionChangedListener
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ActivitySignupBinding
import com.tharsol.endtb.extenstions.getNumberWithoutSpace
import com.tharsol.endtb.extenstions.isValid
import com.tharsol.endtb.network.ApiAdapter
import com.tharsol.endtb.serialize.RequestRegister
import com.tharsol.endtb.util.*
import kotlinx.coroutines.launch


class RegisterActivity : BaseActivity()
{

    var binding: ActivitySignupBinding? = null
    var progressDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        updateTitle(binding?.include?.topBarTitle, getString(R.string.sign_up))
        updateHome(binding?.include?.btnUp, true)
        ViewUtils.updateTbProgramText(binding!!.textViewDescpLabel)
        onClickListeners()

//        binding?.buttonSignupType?.onPositionChangedListener = OnPositionChangedListener {
//            if (it == 0) binding?.editTextLincenseNumber?.setHint("License Number")
//            else binding?.editTextLincenseNumber?.setHint("PMDC Number")
//        }
    }

    /*override fun onUp()
    {
        ActivityUtils.startLoginActivity(this)
        finish()
    }*/

    private fun onClickListeners()
    {
        binding?.btnGo?.setOnClickListener {
            onSubmit()
        }
    }

    /*override fun onBackPressed()
    {
        onUp()
    }*/

    fun onSubmit()
    {

        if (!ValidationUtils.validateMobile(binding?.editTextMobile?.editor))
        {
            binding?.editTextMobile?.setError(getString(R.string.error_empty_field))
            ViewUtils.requestFocus(binding?.editTextMobile)
            return
        }
        binding?.editTextMobile?.setError(null)

//        if (TextUtils.isEmpty(binding?.editTextLincenseNumber?.text))
//        {
//            binding?.editTextLincenseNumber?.setError(getString(R.string.error_empty_field))
//            ViewUtils.requestFocus(binding?.editTextLincenseNumber)
//            return
//        }
//        binding?.editTextLincenseNumber?.setError(null)

        if (TextUtils.isEmpty(binding?.editTextUserName?.text))
        {
            binding?.editTextUserName?.setError(getString(R.string.error_empty_field))
            return
        }
        binding?.editTextUserName?.setError(null)

        if (TextUtils.isEmpty(binding?.editTextUserName?.text))
        {
            binding?.editTextUserName?.setError(getString(R.string.error_empty_field))
            return
        }
        binding?.editTextUserName?.setError(null)

        if (!ValidationUtils.validatePassword(binding?.editTextPassword?.editor))
        {
            binding?.editTextPassword?.setError(getString(R.string.error_password_field))
            return
        }
        binding?.editTextPassword?.setError(null)

        if (!TextUtils.equals(binding?.editTextRepeatPassword?.text, binding?.editTextPassword?.text))
        {
            binding?.editTextRepeatPassword?.setError(getString(R.string.error_repeat_password_field))
            return
        }
        binding?.editTextRepeatPassword?.setError(null)
        loadData()
    }

    private fun loadData()
    {
        lifecycleScope.launch {
            progressDialog = ProgressDialogUtils.createProgressDialog(this@RegisterActivity)
            progressDialog?.show()
            try
            {
                val request = RequestRegister(binding?.editTextMobile?.getNumberWithoutSpace(), binding?.editTextUserName?.text?.toString(), binding?.editTextLincenseNumber?.text?.toString(), Utilities.getDeviceUdId(this@RegisterActivity), binding?.editTextPassword?.text.toString())
                val response = ApiAdapter.apiClient.register(request)

                if (response.isValid())
                {
                    val innerResponse = response.body()
                    if (innerResponse?.successFlag!!)
                    {
                        UserData.saveUser(innerResponse.data)
                        App.isDataClear = false
                        ActivityUtils.startMainActivity(this@RegisterActivity)
                        finish()
                    }
                    else
                    {
                        Utilities.showToast(this@RegisterActivity, innerResponse.activityInfo)
                    }
                }
                else
                { // Show API error.
                    Toast.makeText(this@RegisterActivity, "Error Occurred: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }
            catch (e: Exception)
            {
                Toast.makeText(this@RegisterActivity, "Error Occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }
            progressDialog?.dismiss()
        }
    }
}