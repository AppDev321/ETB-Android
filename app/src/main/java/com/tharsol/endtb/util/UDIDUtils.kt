package com.tharsol.endtb.util

import java.util.*

object UDIDUtils {
    fun generateRandom(): String {
        return UUID.randomUUID().toString().toUpperCase()
    }

//    private fun verifyKey(table: String, columnName: String, udid: String): Boolean {
//        return AppDb.get().workPlans().verifyUniqueKey(table, columnName, udid)
//    }
//
//    fun getVerifiedKey(table: String?, columnName: String?): String {
//        val udid = generateRandom()
//        val isVerified: Boolean = AppDb.get().workPlans().verifyUniqueKey(table, columnName, udid)
//        return if (isVerified) udid else getVerifiedKey(table, columnName)
//    }
}