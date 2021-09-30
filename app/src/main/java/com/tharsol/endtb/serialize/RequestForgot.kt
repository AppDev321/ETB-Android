package com.tharsol.endtb.serialize

import com.google.gson.annotations.SerializedName

data class RequestForgot(

    @field:SerializedName("Username")
    val username: String? = null,

    @field:SerializedName("DeviceId")
    val deviceId: String? = null

    )
