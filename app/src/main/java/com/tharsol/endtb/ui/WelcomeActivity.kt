package com.tharsol.endtb.ui

import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ActivityWelcomeBinding
import com.tharsol.endtb.util.ActivityUtils
import com.tharsol.endtb.util.ViewUtils

class WelcomeActivity : BaseActivity(), View.OnClickListener {

    var binding: ActivityWelcomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        updateTitle(binding?.include?.topBarTitle, "")

        binding?.btnGo?.setOnClickListener {
            ActivityUtils.startLoginActivity(this)
            finish()
        }
//        binding!!.textView2.setOnClickListener(this)
//        binding!!.imageView5.setOnClickListener(this)
//        ViewUtils.updateTbProgramText(binding!!.textViewDescpLabel)
        if (ActivityCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA, READ_PHONE_STATE),
                546
            )
        }
    }

    override fun onClick(v: View?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:".plus(getString(R.string._92_800_55511)))
        startActivity(intent)
    }
}