package com.tharsol.endtb.serialize

import com.google.gson.annotations.SerializedName

data class RequestChangePassword(

    @field:SerializedName("Password")
    val password: String? = null,

    @field:SerializedName("NewPassword")
    val newPassword: String? = null,

    @field:SerializedName("DeviceId")
    val deviceId: String? = null

)
