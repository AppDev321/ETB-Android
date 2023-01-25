package com.tharsol.endtb.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.tharsol.endtb.databinding.FragmentNotificationBinding
import com.tharsol.endtb.extenstions.isValid
import com.tharsol.endtb.model.NotificationDataModel
import com.tharsol.endtb.model.SearchedPatient
import com.tharsol.endtb.network.ApiAdapter
import com.tharsol.endtb.serialize.RequestSearchByDatePatient
import com.tharsol.endtb.ui.NotificationDetailActivity
import com.tharsol.endtb.ui.adapters.NotificationListAdapter
import com.tharsol.endtb.util.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus


class NotificationFragment : BaseListFragment(),
    NotificationListAdapter.OnItemClickListener {

    var binding: FragmentNotificationBinding? = null
    private var adapter: NotificationListAdapter? = null;

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
        binding = FragmentNotificationBinding.inflate(inflater)
        return binding?.root
    }

    override fun setAdaptor() {
        list?.adapter = adapter
    }

    fun getListDate()
    {
        var context = this
        lifecycleScope.launch {

//            val progressDialog: SweetAlertDialog? =
//                ProgressDialogUtils.createProgressDialog(requireContext())
//
//            progressDialog?.show()
            swipe?.isRefreshing = true
            try {
                EventBus.getDefault().post(SearchedPatient(true))
                val response =
                    ApiAdapter.apiClient.searchPatientByDate(RequestSearchByDatePatient("2023-01-25", ""))
                swipe?.isRefreshing = false
                if (response.isValid()) {
                    val innerResponse = response.body()
                    if (innerResponse?.successFlag!!) {
//                        progressDialog?.dismiss()
                        if (innerResponse.data!!.isEmpty()) {

                        } else {

                            var imageURL = "https://www.shutterstock.com/image-illustration/secondary-tuberculosis-lungs-closeup-view-600w-1067326121.jpg";
                            val data = ArrayList<NotificationDataModel>()
                            for (item in innerResponse.data)
                            {
                                if (item.DistrictId.toString() == UserData.user!!.LocalityId)
                                {
                                    data.add(NotificationDataModel(item.id.toString(), item.treatmentStartDate!!,
                                        item.ChemistName!!, item.name!!, item.DistrictName!!,
                                        item.mobileNumber!!, item.age.toString(), imageURL ))

                                }


                            }

                            adapter =  NotificationListAdapter(data, context)
                            adapter?.notifyDataSetChanged()
                            setAdaptor()
                        }
                    } else {
//                        ProgressDialogUtils.showError(
//                            progressDialog!!,
//                            getString(R.string.oops),
//                            innerResponse.activityInfo
//                        )
                        Utilities.showToast(App.context, innerResponse.activityInfo)
//                        visibilityProceed(View.VISIBLE)
                    }
                } else {
                    // Show API error.
                    Utilities.showToast(
                        App.context,
                        response.message()
                    )
//                    visibilityProceed(View.VISIBLE)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    App.context,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
//                visibilityProceed(View.VISIBLE)
            }
//            progressDialog?.dismiss()

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        updateViews(
            binding!!.includeBaseList.list, binding?.includeBaseList?.swipe,
            binding?.includeBaseList?.empty, binding?.includeBaseList?.fab
        )
        super.onViewCreated(view, savedInstanceState)

        loadData()
    }

    override fun loadData() {
        fetchNotification()
    }


    private fun fetchNotification() {
//        val data = ArrayList<NotificationDataModel>()
//        var imageURL = "https://www.shutterstock.com/image-illustration/secondary-tuberculosis-lungs-closeup-view-600w-1067326121.jpg";
//        for (i in 1..20) {
//
//            data.add(NotificationDataModel(" abc " +i, "3rd Junary",
//                         "No Name Store", "Patient ABC", "Rawalpindi",
//                        "0324-04343", "25", imageURL ))
//        }
//        adapter =  NotificationListAdapter(data, this)
//        adapter?.notifyDataSetChanged()
//        setAdaptor()
        getListDate()
    }


    override fun showFab(): Boolean {
        return false
    }

    companion object {

        val TAG = NotificationFragment::class.java.name
        val IS_SEARCHED = "isSearched"
        val KEY_PATIENT = "patient"
        val KEY_MOBILE_NUMBER = "mobile"

        @JvmStatic
        fun newInstance() =
            NotificationFragment()
    }


    override fun onItemClick(notificationData: NotificationDataModel) {
        val intent = Intent(context, NotificationDetailActivity::class.java).apply {
            putExtra("NotificationData", notificationData)

        }
         startActivity(intent)
    }

}