package com.tharsol.endtb.util

import android.app.Application
import android.content.Context
import android.net.http.HttpResponseCache
import android.text.TextUtils
import android.util.Log
import com.hypertrack.hyperlog.HyperLog
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        myApp = this
        //        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID


        //
        HyperLog.initialize(this, 2592000)
        HyperLog.setLogLevel(Log.VERBOSE)
        try
        {
            val httpCacheDir = File(cacheDir, "eTBC")
            val httpCacheSize = 10 * 1024 * 1024.toLong() // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize)
        }
        catch (e: IOException)
        {
            Log.i(TAG, "HTTP response cache installation failed:$e")
        }
    }

    companion object
    {
        private val TAG = App::class.java.simpleName
        private var myApp: Application? = null
        var isDataClear = false
        val context: Context
            get() = myApp!!.applicationContext

        //        fun fixLeakCanary696(context: Context?) {
        //            if (!isEmui) {
        //                Log.w(TAG, "not emui")
        //                return
        //            }
        //            try {
        //                val clazz = Class.forName("android.gestureboost.GestureBoostManager")
        //                Log.w(TAG, "clazz $clazz")
        //                val _sGestureBoostManager = clazz.getDeclaredField("sGestureBoostManager")
        //                _sGestureBoostManager.isAccessible = true
        //                val _mContext = clazz.getDeclaredField("mContext")
        //                _mContext.isAccessible = true
        //                val sGestureBoostManager = _sGestureBoostManager[null]
        //                if (sGestureBoostManager != null) {
        //                    _mContext[sGestureBoostManager] = context
        //                }
        //            } catch (ignored: Exception) {
        //                ignored.printStackTrace()
        //            }
        //        }
        //
        //        val isEmui: Boolean
        //            get() = !TextUtils.isEmpty(getSystemProperty("ro.build.version.emui"))
        //
        //        fun getSystemProperty(propName: String): String? {
        //            val line: String
        //            var input: BufferedReader? = null
        //            try {
        //                val p = Runtime.getRuntime().exec("getprop $propName")
        //                input = BufferedReader(InputStreamReader(p.inputStream, "UTF-8"), 1024)
        //                line = input.readLine()
        //                input.close()
        //            } catch (ex: IOException) {
        //                Log.w(TAG, "Unable to read sysprop $propName", ex)
        //                return null
        //            } finally {
        //                IOUtils.closeQuietly(input)
        //            }
        //            return line
        //        }
    }
}