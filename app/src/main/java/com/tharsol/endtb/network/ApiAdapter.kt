package com.tharsol.endtb.network

import com.tharsol.endtb.BuildConfig
import com.tharsol.endtb.util.App
import com.tharsol.endtb.util.UserData
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiAdapter {
    val SERVER_URL: String = "https://api.etb.com.pk/"
    var logging = HttpLoggingInterceptor()
    var cacheSize: Long = 10 * 1024 * 1024 // 10 MB

    var cache: Cache = Cache(App.context.cacheDir, cacheSize)

    private val getOkHttpClint: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            builder.cache(cache)
            builder.addInterceptor {
                val newRequest = it.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + UserData.user?.token)
                    .build()
                it.proceed(newRequest)
            }
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)
            }
            return builder.build()
        }

    val apiClient: ApiClient =
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(getOkHttpClint)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)

}