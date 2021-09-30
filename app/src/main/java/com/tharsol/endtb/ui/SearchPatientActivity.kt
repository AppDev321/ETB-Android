package com.tharsol.endtb.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ActivityBaseBinding
import com.tharsol.endtb.ui.fragments.SearchFragment
import com.tharsol.endtb.util.ActivityUtils
import com.tharsol.endtb.util.FragmentUtils

class SearchPatientActivity : BaseActivity() {

    var binding: ActivityBaseBinding? = null
    var tag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val title = intent.getStringExtra(KEY_TITLE)
        updateTitle(binding?.include?.topBarTitle, title)
        tag = intent.getStringExtra(KEY_FRAGMENT)
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            FragmentUtils.initFragment(
                supportFragmentManager.fragmentFactory,
                tag!!, intent.extras
            ), tag!!
        ).commit()

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
            val intent = Intent(context, SearchPatientActivity::class.java).apply {
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
            val intent = Intent(context, SearchPatientActivity::class.java).apply {
                putExtra(KEY_FRAGMENT, fragment)
                putExtra(KEY_TITLE, title)
                bundle?.let { putExtras(bundle) }
            }
            ActivityUtils.startActivityForResult(context, intent, requestCode)
        }


    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        val intent = Intent()
        if (fragment != null && fragment is SearchFragment) {
//            intent.putExtra(SearchFragment.IS_SEARCHED, fragment.isSearched)
            intent.putExtra(SearchFragment.KEY_MOBILE_NUMBER, fragment.getNumber())
        }
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
