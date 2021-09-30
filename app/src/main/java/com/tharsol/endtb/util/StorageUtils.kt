package com.tharsol.endtb.util

import com.snatik.storage.EncryptConfiguration
import com.snatik.storage.Storage
import java.io.File

object StorageUtils {
    private const val IVX = "abcdefghijklmnop" // 16 length
    private const val SECRET_KEY = "secret1234567890" // 16 lenght
    private val SALT = "0000111100001111".toByteArray()
    private val configuration = EncryptConfiguration.Builder()
        .setEncryptContent(IVX, SECRET_KEY, SALT)
        .build()
    val config: EncryptConfiguration
        get() = configuration
    val devicePath: String
        get() = File(
            Storage(App.context).getExternalStorageDirectory(),
            ".126abc"
        ).absolutePath
}