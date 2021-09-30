package com.tharsol.endtb.util

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.tharsol.endtb.database.AppDb
import com.tharsol.endtb.deserialize.User
import com.tharsol.endtb.ui.LoginActivity
import com.tharsol.endtb.util.Utilities.getSharedPreferences

object UserData {
    const val PASSWORD = "tttttt"
    const val KEY_USER_ID = "UserId"
    const val KEY_SYNCED_TIME = "syncedTime"
    const val KEY_REAL_SYNCED_TIME = "syncedERealTimeTime"
    const val KEY_SHUT_DOWN_TIME = "syncedSRealTimeTime"
    const val KEY_HAVE_UPDATE = "updated"
    const val KEY_SYNCED_DATE = "syncedDate"
    const val KEY_SYNCED_SERVER_DATE = "syncedServerDate"
    const val KEY_REMEMBER_ME = "Remember"
    const val KEY_IS_DELETED = "Deleted"
    const val KEY_IS_AUG_PLAN_CHANGED = "6,7 August"
    private var aes: String? = ""
    private var USER_INFO: User? = null

    val company: Int
        get() = getSharedPreferences(App.context).getInt(Constants.COMPANY_ID, -1)
    val password: String?
        get() = getSharedPreferences(App.context).getString(Constants.PASSWORD, null)

    fun saveUser(user: User?) {
        try {
            USER_INFO = user
            getSharedPreferences(App.context).edit().putString(
                KEY_USER_ID,
                if (user != null) Gson().toJson(user) else null
            ).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    val user: User?
        get() {
            if (USER_INFO == null) {
                val user = getSharedPreferences(App.context).getString(KEY_USER_ID, null)
                USER_INFO = if (user == null) null else Gson().fromJson(user, User::class.java)
            }
            return USER_INFO
        }

    fun saveHaveUpdate(haveUpdate: Boolean) {
        getSharedPreferences(App.context).edit().putBoolean(KEY_HAVE_UPDATE, haveUpdate)
            .apply()
    }

    fun haveUpdate(): Boolean {
        return getSharedPreferences(App.context).getBoolean(KEY_HAVE_UPDATE, false)
    }

    /**
     * @param date date should be format as yyyy-MM-dd
     */
    fun saveSyncDate(date: String?) {
        getSharedPreferences(App.context).edit().putString(KEY_SYNCED_DATE, date).apply()
    }

    val syncDate: String?
        get() = getSharedPreferences(App.context).getString(KEY_SYNCED_DATE, "")

    /**
     * @param date date should be format as yyyy-MM-dd
     */
    fun saveServerDate(date: String?) {
        getSharedPreferences(App.context).edit().putString(KEY_SYNCED_SERVER_DATE, date)
            .apply()
    }

    val serverDate: String?
        get() = getSharedPreferences(App.context).getString(KEY_SYNCED_SERVER_DATE, "")

    /**
     * @param time sync time as mili seconds
     */
    fun saveRealSyncTime(time: Long) {
        getSharedPreferences(App.context).edit().putLong(KEY_REAL_SYNCED_TIME, time).apply()
    }

    val realSyncTime: Long
        get() = getSharedPreferences(App.context).getLong(KEY_REAL_SYNCED_TIME, 0L)

    /**
     * @param time sync time as mili seconds
     */
    fun saveShutDownSyncTime(time: Long) {
        getSharedPreferences(App.context).edit().putLong(KEY_SHUT_DOWN_TIME, time).apply()
    }

    val shutDownSyncTime: Long
        get() = getSharedPreferences(App.context).getLong(KEY_SHUT_DOWN_TIME, 0L)

    /**
     * @param time sync time as mili seconds
     */
    fun saveSyncTime(time: Long) {
        getSharedPreferences(App.context).edit().putLong(KEY_SYNCED_TIME, time).apply()
    }

    val syncTime: Long
        get() = getSharedPreferences(App.context).getLong(KEY_SYNCED_TIME, 0L)

    fun saveRememberMe(data: String?) {
        getSharedPreferences(App.context).edit().putString(KEY_REMEMBER_ME, data).apply()
    }

    val rememberMe: String?
        get() = getSharedPreferences(App.context).getString(KEY_REMEMBER_ME, "")

    fun saveEmptyDeleted(value: Boolean) {
        getSharedPreferences(App.context).edit().putBoolean(KEY_IS_DELETED, value).apply()
    }

    fun loadOut(context: Context) {
//        Tasks.call {
//
////            AppDb.get().workPlanBase().updateEmptyAccessToken();
//            FirebaseInstanceId.getInstance().deleteInstanceId();
        saveUser(null)
//            //            TokenDataTransmit.clearCache(App.context);
////            SecureDate.getInstance().clear();
//            if (HttpResponseCache.getInstalled() != null) {
//                HttpResponseCache.getInstalled().flush()
//            }
//            Any()
//        }.continueWith { task ->
//            if (task.getResult() != null) {
        getSharedPreferences(context).edit().clear().apply()
        if (!App.isDataClear) {
            context.startActivity(
                Intent(
                    context,
                    LoginActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            App.isDataClear = true
        }
        AppDb.get().clearAllTables()
//            }
//            null
//        }
    }


}