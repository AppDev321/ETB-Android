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

    }
}