package com.tharsol.endtb.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.tharsol.endtb.databinding.ActivitySplashBinding
import com.tharsol.endtb.util.ActivityUtils
import com.tharsol.endtb.util.UserData
import com.tharsol.endtb.util.ViewUtils

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val mHideHandler = Handler(Looper.getMainLooper())
//    private val mHidePart2Runnable = Runnable {
//        // Delayed removal of status and navigation bar
//
//        // Note that some of these constants are new as of API 16 (Jelly Bean)
//        // and API 19 (KitKat). It is safe to use them, as they are inlined
//        // at compile-time and do nothing on earlier devices.
//
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LOW_PROFILE or
//                    View.SYSTEM_UI_FLAG_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        mHideHandler.postDelayed(Runnable {
//            if (UserData.user == null) {
//                ActivityUtils.startWelcomeActivity(this@SplashActivity)
//            } else {
//                ActivityUtils.startMainActivity(this@SplashActivity)
//            }
//            finish()
//        }, AUTO_HIDE_DELAY_MILLIS1)
//    }
//    private val mShowPart2Runnable = Runnable {
//        // Delayed display of UI elements
//        supportActionBar?.show()
//
//    }
//    private var mVisible: Boolean = false
//    private val mHideRunnable = Runnable { hide() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
//       ViewUtils.updateTbProgramText(binding.textViewDescpLabel)
//        mVisible = true

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
//        delayedHide(100)
        mHideHandler.postDelayed(Runnable {
            if (UserData.user == null) {
                ActivityUtils.startWelcomeActivity(this@SplashActivity)
            } else {
                ActivityUtils.startMainActivity(this@SplashActivity)
            }
            finish()
        }, AUTO_HIDE_DELAY_MILLIS1)
    }


//    private fun hide() {
//        // Hide UI first
//        supportActionBar?.hide()
//        mVisible = false
//
//        // Schedule a runnable to remove the status and navigation bar after a delay
//        mHideHandler.removeCallbacks(mShowPart2Runnable)
//        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
//    }


    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {

//        mHideHandler.removeCallbacks(mHideRunnable)
//        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        private val AUTO_HIDE_DELAY_MILLIS1 = 2000L

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }
}
