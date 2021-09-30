package com.tharsol.endtb.deserialize

import kotlinx.android.parcel.Parcelize

@Parcelize
class LoginResponse : BaseResponse() {
    val data: User? = null
}