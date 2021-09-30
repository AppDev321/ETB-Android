package com.tharsol.endtb.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.FragmentStockBinding
import com.tharsol.endtb.extenstions.isValid
import com.tharsol.endtb.model.AddPatientEvent
import com.tharsol.endtb.model.SearchedPatient
import com.tharsol.endtb.network.ApiAdapter
import com.tharsol.endtb.ui.adapters.StockAdapter
import com.tharsol.endtb.util.ActivityUtils
import com.tharsol.endtb.util.App
import com.tharsol.endtb.util.DateUtilz
import com.tharsol.endtb.util.Utilities
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.joda.time.DateTime
import java.util.*


class StockFragment : BaseListFragment(), View.OnClickListener {

    var binding: FragmentStockBinding? = null
    val adapter = StockAdapter()
    var date: DateTime = DateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockBinding.inflate(inflater)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateViews(
            binding?.includeBaseList?.list, binding?.includeBaseList?.swipe,
            binding?.includeBaseList?.empty, binding?.includeBaseList?.fab
        )
        super.onViewCreated(view, savedInstanceState)
        binding?.btnDate?.setOnClickListener(this)
        binding?.btnAdd?.setOnClickListener(this)
        binding?.tvDate?.text = DateUtilz.formatStandardViewDate(date)
    }

    override fun setAdaptor() {
        adapter.setEmpty(empty)
        list?.adapter = adapter
    }

    override fun loadData() {
        lifecycleScope.launch {
            if (adapter.itemCount > 0) return@launch
            swipe?.isRefreshing = true

            try {
                EventBus.getDefault().post(SearchedPatient(true))
                val response =
                    ApiAdapter.apiClient.getDashProducts(DateUtilz.formatBackendStandard(date))

                if (response.isValid()) {
                    val innerResponse = response.body()
                    if (innerResponse?.successFlag!!) {
                        adapter.items.addAll(innerResponse.data!!)
                        adapter.notifyDataSetChanged()
                    } else {
                        Utilities.showToast(
                            context,
                            innerResponse.activityInfo
                        )
//                        Utilities.showToast(this@LoginActivity, innerResponse.activityInfo)
                    }
                } else {
                    // Show API error.
                    Utilities.showToast(
                        App.context,
                        response.message()
                    )
                }
            } catch (e: Exception) {
                Toast.makeText(
                    App.context,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            swipe?.isRefreshing = false
        }
    }

    override fun showFab(): Boolean {
        return false
    }

    companion object {

        @JvmStatic
        val TAG =StockFragment::class.java.name

        @JvmStatic
        fun newInstance() =
            StockFragment()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {
                EventBus.getDefault().post(AddPatientEvent())
            }
            R.id.btnDate -> {
                showDatePicker()
            }
            else -> {
            }
        }
    }

    private fun showDatePicker() {
        val newFragment = DatePickerFragment()
        newFragment.setOnCreateDialogListener(object : DatePickerFragment.OnCreateDialogListener {
            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                val c = Calendar.getInstance()
                val year = c[Calendar.YEAR]
                val month = c[Calendar.MONTH]
                val day = c[Calendar.DAY_OF_MONTH]
                return DatePickerDialog(
                    requireContext(),
                    { _, year1: Int, month1: Int, dayOfMonth: Int ->
                        date = DateTime.now().withDate(year1, month1 + 1, dayOfMonth)
                        binding?.tvDate?.text = DateUtilz.formatStandardViewDate(date)
                        loadData()
                    }, year, month, day
                )
            }
        })

        newFragment.show(parentFragmentManager, "datePicker")
    }
}