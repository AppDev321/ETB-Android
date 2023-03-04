package com.tharsol.endtb.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.ActivityMainBinding
import com.tharsol.endtb.model.AddPatientEvent
import com.tharsol.endtb.model.LocalResponse
import com.tharsol.endtb.model.RefreshProduct
import com.tharsol.endtb.network.FullSync
import com.tharsol.endtb.ui.fragments.NotificationFragment
import com.tharsol.endtb.ui.fragments.StockFragment
import com.tharsol.endtb.ui.fragments.TbFormFragment
import com.tharsol.endtb.util.*
//import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.joda.time.DateTime
import java.text.MessageFormat

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, TbFormFragment.TopSearchListener
{


    private var binding: ActivityMainBinding? = null
    var syncing: FullSync? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root) //        binding?.navView?.changeCornerRadius()
        updateTileHeadColor(binding?.navView?.menu)
        binding?.includeMain?.include?.btnUp?.setOnClickListener {
            binding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
        binding?.navView?.setNavigationItemSelectedListener(this)
//        binding!!.navView.getHeaderView(0).appVersion.text = MessageFormat.format("v{0}", Utilities.getCurrentAppVersion(this))
        if (savedInstanceState == null) {


            if (UserData.user?.DesignationId == "1" || UserData.user?.DesignationId == "4")
            {
                addNotificationFragment()
            }
            else{

                addFormFragment()
            }
        }

        updateUserInfo()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE), 546)
        }
        sync()
        EventBus.getDefault().register(this)
    }

    private fun addStockFragment()
    {
        binding!!.includeMain.include.topBarTitle.text = getString(R.string.stock_position)
        binding!!.includeMain.include.topSearch.visibility = View.GONE
        binding!!.includeMain.include.mainPharmacyInfo.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().replace(R.id.container, StockFragment.newInstance(), StockFragment.TAG).commit()
    }

    private fun addNotificationFragment()
    {
        binding!!.includeMain.include.topBarTitle.text = getString(R.string.tb_notification_form)
        binding!!.includeMain.include.topSearch.visibility = View.GONE
        binding!!.includeMain.include.mainPharmacyInfo.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().replace(R.id.container, NotificationFragment.newInstance(), NotificationFragment.TAG).commit()
    }

    private fun addFormFragment()
    {
        binding!!.includeMain.include.topBarTitle.text = getString(R.string.tb_notification_form)
        binding!!.includeMain.include.mainPharmacyInfo.visibility = View.VISIBLE
        binding!!.includeMain.include.topSearch.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().replace(R.id.container, TbFormFragment.newInstance(), TbFormFragment.TAG).commit()
    }

    private fun sync()
    {
        val fullSyncValue = Utilities.getLastSyncDaysDuration(this@MainActivity) //        if (mUser.getDesignationLevel() < 6 || fullSyncValue == 999) {
        if (/*mFullSync ||*/ fullSyncValue > Utilities.SYNC_DAYS)
        {
            fullSync()
            return
        } //        }
    }

    private fun fullSync()
    {

        if (syncing != null && syncing!!.isSyncing)
        {
            Utilities.showToast(this, getString(R.string.sync_inpprogress))
            return
        }

        syncing = object : FullSync(mUser = UserData.user!!)
        {
            override fun onComplete(response: LocalResponse)
            {
                when (response.result)
                {
                    RESULT_ERROR_NORMAL ->
                    {
                        response.message
                    }
                    else                ->
                    {
                        Utilities.getEditor(this@MainActivity).putString(Constants.LAST_SYNC_DATE, DateTime.now().toString("yyyy-MM-dd")).apply()
                        syncing?.clearUpdateCache()
                        EventBus.getDefault().post(RefreshProduct())
                    }
                }

            }
        }
    }

    @Subscribe fun onEvent(item: AddPatientEvent)
    {
        addFormFragment()
    }

    private fun updateTileHeadColor(menu: Menu?)
    {
        menu?.let {
            for (itemNumber in 0 until it.size())
            {
                val groupItems = it.getItem(itemNumber)
                groupItems.title = Utilities.changeTextSize(Utilities.changeForeground(groupItems.title!!, ContextCompat.getColor(this, R.color.colorPrimary)), resources.getDimension(R.dimen.text_size_18sp).toInt())

                if (itemNumber == 1)
                {
                    groupItems.isVisible =
                        !(UserData.user?.DesignationId == "1" || UserData.user?.DesignationId == "4")
                }
                else if (itemNumber == 0)
                {
                    groupItems.isVisible = (UserData.user?.DesignationId == "1" || UserData.user?.DesignationId == "4")
                }
            }
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed()
    {
        if (binding?.drawerLayout!!.isDrawerOpen(GravityCompat.START))
        {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        return when (item.itemId)
        {

            R.id.notify ->
            {
                addNotificationFragment()
                true
            }

            R.id.nav_sync           ->
            {
                fullSync()
                true
            }
            R.id.change_password    ->
            {
                ActivityUtils.startChangePasswordActivity(this)
                true
            }

            R.id.tbForm             ->
            {
                addFormFragment()
                true
            }
            R.id.logout             ->
            {
                UserData.loadOut(this)
                finish()
                true
            }
            else                    -> false
        }
    }

    private fun updateUserInfo()
    {
//        if (UserData.user!!.chemistName.isNullOrBlank())
//        {
            binding?.includeMain?.include?.pharmacyName?.text = UserData.user!!.username!!.uppercase()
//        }
//        else{
//            binding?.includeMain?.include?.pharmacyName?.text = UserData.user?.chemistName
//        }
        binding?.includeMain?.include?.date?.text = DateUtilz.formatHeaderViewDate(DateTime.now())


    }

    override fun getSearchView(): EditText
    {
        return binding!!.includeMain.include.topSearch
    }
}