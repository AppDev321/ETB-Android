package com.tharsol.endtb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tharsol.endtb.R;
import com.tharsol.endtb.databinding.ActivityFullScreenImageBinding
import com.tharsol.endtb.databinding.ActivityWelcomeBinding
import com.tharsol.endtb.model.NotificationDataModel
import com.tharsol.endtb.util.ActivityUtils
import com.tharsol.endtb.util.ImageUtil

class FullScreenImageActivity : BaseActivity() {

    var binding: ActivityFullScreenImageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        updateTitle(binding?.include?.topBarTitle, "")


        initData()
    }

    fun initData()
    {
        val image = intent.getStringExtra("image")

        if(image != null && !image.isEmpty())
        {

            binding?.imageView?.let { ImageUtil.loadImage(it, image) }

        }
    }
}