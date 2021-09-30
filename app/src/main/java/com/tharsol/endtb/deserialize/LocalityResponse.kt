package com.tharsol.endtb.deserialize

import kotlinx.android.parcel.Parcelize


@Parcelize
class LocalityResponse : BaseResponse() {
    val data: List<Locality>? = null
}