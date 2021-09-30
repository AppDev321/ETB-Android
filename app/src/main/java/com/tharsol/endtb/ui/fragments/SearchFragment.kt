package com.tharsol.endtb.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.tharsol.endtb.R
import com.tharsol.endtb.databinding.FragmentSearchBinding
import com.tharsol.endtb.deserialize.Patient
import com.tharsol.endtb.extenstions.getNumberWithoutSpace
import com.tharsol.endtb.extenstions.isValid
import com.tharsol.endtb.model.SearchedPatient
import com.tharsol.endtb.network.ApiAdapter
import com.tharsol.endtb.ui.adapters.PatientSearchAdapter
import com.tharsol.endtb.util.App
import com.tharsol.endtb.util.Utilities
import com.tharsol.endtb.util.ValidationUtils
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus


class SearchFragment : BaseListFragment(), View.OnClickListener,
    PatientSearchAdapter.OnItemClickListener {

    var binding: FragmentSearchBinding? = null
    private val adapter = PatientSearchAdapter(this)

//    /**
//     * this is check for force to search patient first
//     */
//    var isSearched: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        updateViews(
            binding!!.includeBaseList.list, binding?.includeBaseList?.swipe,
            binding?.includeBaseList?.empty, binding?.includeBaseList?.fab
        )
        super.onViewCreated(view, savedInstanceState)

        binding?.btnSearch!!.setOnClickListener(this)
        binding?.btnProceed!!.setOnClickListener(this)
        binding?.btnProceed1!!.setOnClickListener(this)
        binding?.etMobileNumber!!.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(v)
                    return true
                }
                return false
            }
        })
        binding?.etMobileNumber?.requestFocus()
    }

    override fun setAdaptor() {
        //adapter.setEmpty(empty)
        list?.adapter = adapter
    }

    override fun loadData() {

    }

    fun getNumber(): String {
        return if (ValidationUtils.validateMobile(binding?.etMobileNumber)) binding!!.etMobileNumber.text.toString() else ""
    }

    private fun fetchPatient(mobileNumber: String) {
        lifecycleScope.launch {

//            val progressDialog: SweetAlertDialog? =
//                ProgressDialogUtils.createProgressDialog(requireContext())
//
//            progressDialog?.show()
            swipe?.isRefreshing = true
            try {
                EventBus.getDefault().post(SearchedPatient(true))
                val response =
                    ApiAdapter.apiClient.searchPatient(mobileNumber)
                swipe?.isRefreshing = false
                if (response.isValid()) {
                    val innerResponse = response.body()
                    if (innerResponse?.successFlag!!) {
//                        progressDialog?.dismiss()
                        if (innerResponse.data!!.isEmpty()) {
                            visibilityProceed(View.VISIBLE)
                        } else {
                            adapter.items.addAll(innerResponse.data)
                            adapter.notifyDataSetChanged()
                            binding!!.btnProceed1.visibility = View.VISIBLE
                        }
                    } else {
//                        ProgressDialogUtils.showError(
//                            progressDialog!!,
//                            getString(R.string.oops),
//                            innerResponse.activityInfo
//                        )
                        Utilities.showToast(App.context, innerResponse.activityInfo)
                        visibilityProceed(View.VISIBLE)
                    }
                } else {
                    // Show API error.
                    Utilities.showToast(
                        App.context,
                        response.message()
                    )
                    visibilityProceed(View.VISIBLE)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    App.context,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                visibilityProceed(View.VISIBLE)
            }
//            progressDialog?.dismiss()

        }

    }


    override fun showFab(): Boolean {
        return false
    }

    companion object {

        val TAG = SearchFragment::class.java.name
        val IS_SEARCHED = "isSearched"
        val KEY_PATIENT = "patient"
        val KEY_MOBILE_NUMBER = "mobile"

        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSearch -> {
                search(v)
            }
            R.id.btnProceed1, R.id.btnProceed -> {
                if (!ValidationUtils.validateMobile(binding!!.etMobileNumber)) {
                    binding!!.etMobileNumber.error = getString(R.string.error_mobile_msg)
                    return
                }
                activity?.setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(KEY_MOBILE_NUMBER, binding!!.etMobileNumber.text.toString())
                })
                activity?.finish()
            }
            else -> {
            }
        }
    }

    private fun visibilityProceed(visibility: Int) {
        binding!!.btnProceed.visibility = visibility
        binding!!.textView3.visibility = visibility
        binding!!.imageView6.visibility = visibility
    }

    private fun search(v: View?) {
        visibilityProceed(View.GONE)
        if (!ValidationUtils.validateInternet(requireContext())) return
        if (!ValidationUtils.validateMobile(binding!!.etMobileNumber)) {
            binding!!.etMobileNumber.error = getString(R.string.error_mobile_msg)
            return
        }
        Utilities.hideKeyboard(v)
        adapter.items.clear()
        adapter.notifyDataSetChanged()

        fetchPatient(binding!!.etMobileNumber.getNumberWithoutSpace())
    }

    override fun onItemClick(patient: Patient) {
        activity?.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(KEY_PATIENT, patient)
        })
        activity?.finish()
    }
}