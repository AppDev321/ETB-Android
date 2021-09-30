package com.tharsol.endtb.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ActivityBaseBinding
import com.tharsol.endtb.util.ActivityUtils
import com.tharsol.endtb.util.FragmentUtils

class FrameActivity : BaseActivity() {

    var binding: ActivityBaseBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val title = intent.getStringExtra(KEY_TITLE)
        updateHome(binding!!.include.btnUp, true)
        updateTitle(binding?.include?.topBarTitle, title)

        supportFragmentManager.beginTransaction().replace(
            R.id.container, FragmentUtils.initFragment(
                supportFragmentManager.fragmentFactory,
                intent.getStringExtra(KEY_FRAGMENT)!!, intent.extras
            )
        ).commit()

    }

    override fun onUp() {
        onBackPressed()
    }

    companion object {
        @JvmField
        val KEY_FRAGMENT = "kFrag"
        val KEY_TITLE = "kTitle"

        fun startActivity(
            context: Context,
            title: String,
            fragment: String,
            bundle: Bundle? = null
        ) {
            val intent = Intent(context, FrameActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, fragment)
                putExtra(KEY_TITLE, title)
                bundle?.let { putExtras(bundle) }
            }
            ActivityUtils.startActivity(context, intent)
        }

        fun startActivityForResult(
            context: Activity,
            title: String,
            fragment: String,
            requestCode: Int,
            bundle: Bundle? = null
        ) {
            val intent = Intent(context, FrameActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, fragment)
                putExtra(KEY_TITLE, title)
                bundle?.let { putExtras(bundle) }
            }
            ActivityUtils.startActivityForResult(context, intent, requestCode)
        }

        fun startActivityForResult(
            context: Fragment,
            title: String,
            fragment: String,
            requestCode: Int,
            bundle: Bundle? = null
        ) {
            val intent = Intent(context.context, FrameActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, fragment)
                putExtra(KEY_TITLE, title)
                bundle?.let { putExtras(bundle) }
            }
            context.startActivityForResult(intent, requestCode)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
