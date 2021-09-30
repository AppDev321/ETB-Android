package com.tharsol.endtb.serialize

import com.google.gson.annotations.SerializedName

data class RequestLogin(

    @field:SerializedName("Username")
    val username: String? = null,

    @field:SerializedName("Password")
    val password: String? = null,

    @field:SerializedName("DeviceId")
    val deviceId: String? = null

    )
