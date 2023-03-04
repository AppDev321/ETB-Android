package com.tharsol.endtb.ui

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ActivityFullScreenImageBinding
import com.tharsol.endtb.util.ImageUtil
import java.io.*


class FullScreenImageActivity : BaseActivity() {

    //Permission Request Handler
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    var binding: ActivityFullScreenImageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        updateTitle(binding?.include?.topBarTitle, "")

        updateHome(binding?.include?.saveImage, true)

        //Setting up Activity Permission Request Handler
        setPermissionCallback()

        initData()

        onClickListeners()


    }

    private fun onClickListeners() {
        binding?.include?.saveImage?.setOnClickListener {
            this.saveImage()
        }
    }

    fun initData() {
        val image = intent.getStringExtra("image")

        if (image != null && !image.isEmpty()) {

            binding?.imageView?.let { ImageUtil.loadImage(it, image) }
            binding?.imageView?.presentScale = 1.0F;

        }
    }



    fun saveImage() {

        val drawable = binding?.imageView?.drawable
        val bitmap = (drawable as BitmapDrawable).bitmap

        checkPermissionAndSaveBitmap(bitmap)

    }

    fun saveMediaToStorage(bitmap: Bitmap) {
        //Generating a file name
        val name = intent.getStringExtra("name")
        val filename = "$name  ${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            this.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            this?.toast("Saved to Photos")
        }
    }




    fun toast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun showPermissionRequestDialog(
        title: String,
        body: String,
        callback: () -> Unit
    ) {
        AlertDialog.Builder(this).also {
            it.setTitle(title)
            it.setMessage(body)
            it.setPositiveButton("Ok") { _, _ ->
                callback()
            }
        }.create().show()
    }

    //Allowing activity to automatically handle permission request
    private fun setPermissionCallback() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    this.saveImage()
                }
            }
    }


    //function to check and request storage permission

    private fun checkPermissionAndSaveBitmap(bitmap: Bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    this.saveMediaToStorage(bitmap)
                }


                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    showPermissionRequestDialog(
                        getString(R.string.permission_title),
                        getString(R.string.write_permission_request)
                    ) {
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }
    }
}