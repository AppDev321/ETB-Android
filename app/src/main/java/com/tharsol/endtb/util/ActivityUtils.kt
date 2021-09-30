package com.tharsol.endtb.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tharsol.endtb.ui.*

object ActivityUtils {

    fun startActivity(context: Context, intent: Intent) {
        context.startActivity(intent)
    }

    fun startActivityForResult(context: Activity, intent: Intent, requestCode: Int) {
        context.startActivityForResult(intent, requestCode)
    }

    fun startWelcomeActivity(context: Activity) {
        val intent = Intent(context, WelcomeActivity::class.java)
        context.startActivity(intent)
    }

    fun startForgotActivity(context: Context) {
        startActivity(context, Intent(context, ForgotPasswordActivity::class.java))
    }

    fun startMainActivity(context: Context) {
        startActivity(context, Intent(context, MainActivity::class.java))
    }

    fun startLoginActivity(context: Context) {
        startActivity(context, Intent(context, LoginActivity::class.java))
    }

    fun startSignUPActivity(context: Context) {
        startActivity(context, Intent(context, RegisterActivity::class.java))
    }

    fun startChangePasswordActivity(context: Context) {
        startActivity(context, Intent(context, ChangePasswordActivity::class.java))
    }

}