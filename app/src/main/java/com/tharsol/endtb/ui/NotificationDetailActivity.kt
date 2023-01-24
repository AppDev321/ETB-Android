package com.tharsol.endtb.ui

import android.content.Intent
import android.os.Bundle
import com.tharsol.endtb.databinding.ActivityNotificationDetailBinding
import com.tharsol.endtb.model.NotificationDataModel
import com.tharsol.endtb.util.ImageUtil

class NotificationDetailActivity : BaseActivity() {
    var binding: ActivityNotificationDetailBinding? = null
    var tag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initData()
    }

    fun initData()
    {
        val notificationData = intent.getParcelableExtra<NotificationDataModel>("NotificationData")

        if(notificationData != null)
        {
            binding?.textAge?.text = notificationData.age
            binding?.textPatientName?.text = notificationData.patientName
            binding?.textDistrict?.text = notificationData.districit
            binding?.textContact?.text = notificationData.contactNo
            binding?.textPharmacy?.text = notificationData.pharmacy
            binding?.imageView?.let { ImageUtil.loadImage(it, notificationData.image) }

            binding?.imageView?.setOnClickListener {

                if(notificationData.image != null && !notificationData.image.isEmpty())
                {
                    val intent = Intent(this@NotificationDetailActivity, FullScreenImageActivity::class.java).apply {
                        putExtra("image", notificationData.image)
                    }
                    startActivity(intent)
                }

            }
        }


    }
}