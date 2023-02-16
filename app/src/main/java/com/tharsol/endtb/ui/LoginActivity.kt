package com.tharsol.endtb.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ActivityLoginBinding
import com.tharsol.endtb.extenstions.isValid
import com.tharsol.endtb.network.ApiAdapter
import com.tharsol.endtb.serialize.RequestLogin
import com.tharsol.endtb.util.*
import kotlinx.coroutines.launch


class LoginActivity : BaseActivity()
{

    var binding: ActivityLoginBinding? = null
    var progressDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        updateTitle(binding?.include?.topBarTitle, getString(R.string.login_title))
        updateHome(binding?.include?.btnUp, true)
        onClickListeners()
        ViewUtils.updateTbProgramText(binding!!.textViewDescpLabel)
        binding?.btnSignup?.text = TextUtils.concat(binding?.btnSignup?.text, " ", Utilities.changeForeground(getString(R.string.sign_up), ContextCompat.getColor(this, R.color.rb_darken_color)))
    }

    /*override fun onUp()
    {
        ActivityUtils.startWelcomeActivity(this)
        //finish()
    }*/

    /*override fun onBackPressed()
    {
        onUp()
    }*/

    private fun onClickListeners()
    {
        binding?.btnSignup?.setOnClickListener {
            ActivityUtils.startSignUPActivity(this)
            //finish()
        }
        binding?.btnGo?.setOnClickListener {
            onDataSubmit()
        }
        binding?.btnForgotPassword?.setOnClickListener {
            ActivityUtils.startForgotActivity(this)
            //finish()
        }
    }

    private fun onDataSubmit()
    {
        if (TextUtils.isEmpty(binding?.username?.text))
        {
            binding?.username?.setError(getString(R.string.error_empty_field))
            return
        }
        binding?.username?.setError(null)

        /*if (!ValidationUtils.validatePassword(binding?.password?.editor))
        {
            binding?.password?.setError(getString(R.string.error_password_field))
            return
        }*/
        if (TextUtils.isEmpty(binding?.password?.text))
        {
            binding?.password?.setError(getString(R.string.error_empty_field))
            return
        }
        binding?.password?.setError(null)

        loadData()
    }

    private fun loadData()
    {
        lifecycleScope.launch {
            progressDialog = ProgressDialogUtils.createProgressDialog(this@LoginActivity)
            progressDialog?.setOnShowListener { dialog ->
                val alertDialog = dialog as SweetAlertDialog
                val text: TextView = alertDialog.findViewById<View>(R.id.title_text) as TextView
                text.textAlignment = View.TEXT_ALIGNMENT_CENTER
                text.isSingleLine = false
            }
            progressDialog?.show()
            try
            {
                val request = RequestLogin(binding?.username?.text?.toString(), binding?.password?.text.toString(), Utilities.getDeviceUdId(this@LoginActivity))
                val response = ApiAdapter.apiClient.login_p(request)

                if (response.isValid())
                {
                    val innerResponse = response.body()
                    if (innerResponse?.successFlag!!)
                    {
                        if (innerResponse.data?.DesignationId == "1" || innerResponse.data?.DesignationId == "4")
                        {
                            UserData.saveUser(innerResponse.data)
                            App.isDataClear = false
                            ActivityUtils.startMainActivity(this@LoginActivity)
                            finish()
                            progressDialog?.dismiss()
                        }
                        else
                        {
                            attemptLoginWithOld()
                        }
                    }
                    else
                    {
                        ProgressDialogUtils.showError(progressDialog!!, getString(R.string.oops), innerResponse.activityInfo) //                        Utilities.showToast(this@LoginActivity, innerResponse.activityInfo)
                    }
                }
                else
                { // Show API error.
                    ProgressDialogUtils.showError(progressDialog!!, getString(R.string.oops), response.message())
                }
            }
            catch (e: Exception)
            {
                Toast.makeText(this@LoginActivity, "Error Occurred: ${e.message}", Toast.LENGTH_LONG).show()
                progressDialog?.dismiss()
            }
        }
    }

    fun attemptLoginWithOld()
    {
        lifecycleScope.launch {
            progressDialog = ProgressDialogUtils.createProgressDialog(this@LoginActivity)
            progressDialog?.setOnShowListener { dialog ->
                val alertDialog = dialog as SweetAlertDialog
                val text: TextView = alertDialog.findViewById<View>(R.id.title_text) as TextView
                text.textAlignment = View.TEXT_ALIGNMENT_CENTER
                text.isSingleLine = false
            }
            progressDialog?.show()
            try
            {
                val request = RequestLogin(binding?.username?.text?.toString(), binding?.password?.text.toString(), Utilities.getDeviceUdId(this@LoginActivity))
                val response = ApiAdapter.apiClient.login(request)

                if (response.isValid())
                {
                    val innerResponse = response.body()
                    if (innerResponse?.successFlag!!)
                    {
                        UserData.saveUser(innerResponse.data)
                        App.isDataClear = false
                        ActivityUtils.startMainActivity(this@LoginActivity)
                        finish()
                        progressDialog?.dismiss()
                    }
                    else
                    {
                        ProgressDialogUtils.showError(progressDialog!!, getString(R.string.oops), innerResponse.activityInfo) //                        Utilities.showToast(this@LoginActivity, innerResponse.activityInfo)
                    }
                }
                else
                { // Show API error.
                    ProgressDialogUtils.showError(progressDialog!!, getString(R.string.oops), response.message())
                }
            }
            catch (e: Exception)
            {
                Toast.makeText(this@LoginActivity, "Error Occurred: ${e.message}", Toast.LENGTH_LONG).show()
                progressDialog?.dismiss()
            }
        }
    }
}