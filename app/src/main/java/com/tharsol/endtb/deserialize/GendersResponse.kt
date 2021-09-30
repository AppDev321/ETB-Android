package com.tharsol.endtb.deserialize

import kotlinx.android.parcel.Parcelize

@Parcelize
class GendersResponse : BaseResponse() {
    val data: List<Gender>? = null
}