package com.tharsol.endtb.network

import android.app.Activity
import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import com.tharsol.endtb.database.AppDb
import com.tharsol.endtb.deserialize.User
import com.tharsol.endtb.extenstions.isValid
import com.tharsol.endtb.model.LocalResponse
import com.tharsol.endtb.model.SyncingEvent
import com.tharsol.endtb.model.UpdateStatus
import com.tharsol.endtb.util.App
import com.tharsol.endtb.util.UserData
import com.tharsol.endtb.util.Utilities
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

abstract class FullSync(var mUser: User, val parent: View? = null) {

    private val mSharedPreferences = Utilities.getSharedPreferences(App.context)
    private lateinit var status: UpdateStatus
    private val mGson: Gson = Gson()
    var mActivity: Activity? = null
    var isSyncing = false
    var numbersOfApi = 0
    lateinit var context: CoroutineContext

    init {
        context = start()
    }

    fun start() = CoroutineScope(Dispatchers.Main).launch {
        initStatus()
        val time = measureTimeMillis {
            isSyncing = true
            val syncDate = UserData.syncDate
            parent?.visibility = View.VISIBLE
            numbersOfApi = 0
            EventBus.getDefault().post(SyncingEvent(numbersOfApi))

            val list: List<Deferred<LocalResponse>> = listOf(async(Dispatchers.IO) {
                products()
            }, async(Dispatchers.IO) {
                localities()
            }, async(Dispatchers.IO) {
                genders()
            })
            val response1 = list.awaitAll().find { it.result != RESULT_OK }

            onComplete(response1 ?: LocalResponse(RESULT_OK, "Successfully"))


            isSyncing = false
            parent?.visibility = View.GONE
        }

        println("Sync Time: ".plus(time / 1000))
    }

    private fun initStatus() {
        val completed = mSharedPreferences.getString("CompletedUrls", null)
        status = if (!TextUtils.isEmpty(completed)) Gson().fromJson(
            completed,
            UpdateStatus::class.java
        ) else UpdateStatus()
        if (status.urls == null) {
            status.urls = ArrayList<String>()
        }
    }

    companion object {
        val mTotal: Int = 29
        const val RESULT_OK = 0
        const val RESULT_ERROR_NORMAL = -4
        const val RESULT_ERROR_TIME = -5
    }

    @Synchronized
    private fun postEvent() {
        ++numbersOfApi
        EventBus.getDefault().post(SyncingEvent(numbersOfApi))
    }

    public fun onDestroy() {
        if (context.isActive) context.cancel()
    }


    private fun showError(response: LocalResponse) {
        when (response.result) {
            RESULT_OK -> ""
        }
    }

    private suspend fun products(): LocalResponse {
        try {
            var result: Long = 0;
            val url = ServiceUrls.GET_PRODUCTS
            if (!isRequestCompleted(url, status)) {

                val responseStr = ApiAdapter.apiClient.getProducts()
                if (responseStr.isValid()) {
                    if (!responseStr.body()?.successFlag!!) {
                        return LocalResponse(RESULT_ERROR_NORMAL, responseStr.body()?.activityInfo)
                    }
                    result = AppDb.get().products().insert(responseStr.body()?.data!!).sum()
                } else {
                    return LocalResponse(RESULT_ERROR_NORMAL, responseStr.message())
                }
                saveRequestCompletedStatus(url, status, result)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return LocalResponse(RESULT_ERROR_NORMAL, "Products")
        }
        postEvent()
        return LocalResponse(RESULT_OK, "Successfully")
    }

    private suspend fun localities(): LocalResponse {
        try {
            var result: Long = 0;
            val url = ServiceUrls.GET_PRODUCTS
            if (!isRequestCompleted(url, status)) {

                val responseStr = ApiAdapter.apiClient.getLocalities()
                if (responseStr.isValid()) {
                    if (!responseStr.body()?.successFlag!!) {
                        return LocalResponse(RESULT_ERROR_NORMAL, responseStr.body()?.activityInfo)
                    }
                    result = AppDb.get().localities().insert(responseStr.body()?.data!!).sum()
                } else {
                    return LocalResponse(RESULT_ERROR_NORMAL, responseStr.message())
                }
                saveRequestCompletedStatus(url, status, result)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return LocalResponse(RESULT_ERROR_NORMAL, "Locality")
        }
        postEvent()
        return LocalResponse(RESULT_OK, "Successfully")
    }

    private suspend fun genders(): LocalResponse {
        try {
            var result: Long = 0;
            val url = ServiceUrls.GET_GENDERS
            if (!isRequestCompleted(url, status)) {

                val responseStr = ApiAdapter.apiClient.getGender()
                if (responseStr.isValid()) {
                    if (!responseStr.body()?.successFlag!!) {
                        return LocalResponse(RESULT_ERROR_NORMAL, responseStr.body()?.activityInfo)
                    }
                    result = AppDb.get().genders().insert(responseStr.body()?.data!!).sum()
                } else {
                    return LocalResponse(RESULT_ERROR_NORMAL, responseStr.message())
                }
                saveRequestCompletedStatus(url, status, result)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return LocalResponse(RESULT_ERROR_NORMAL, "Genders")
        }
        postEvent()
        return LocalResponse(RESULT_OK, "Successfully")
    }


    @Synchronized
    private fun saveRequestCompletedStatus(url: String, status: UpdateStatus, result: Long) {
        if (result <= 0) {
            return
        }
        status.urls?.add(url)
        val json = mGson.toJson(status, UpdateStatus::class.java)
        val editor = mSharedPreferences.edit()
        editor.putString("CompletedUrls", json)
        editor.apply()
        Utilities.printDebug("CompletedUrls:" + status.urls?.size)
    }

     fun clearUpdateCache() {
        val editor = mSharedPreferences.edit()
        editor.putString("CompletedUrls", "")
        editor.apply()
    }

    private fun isRequestCompleted(url: String, status: UpdateStatus?): Boolean {
        return status != null && status.urls!!.contains(url)
    }

    abstract fun onComplete(response: LocalResponse)

}
