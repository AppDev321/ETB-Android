package com.tharsol.endtb.deserialize

import kotlinx.android.parcel.Parcelize

@Parcelize
class FindPatientResponse : BaseResponse() {
    val data: List<Patient>? = null
}