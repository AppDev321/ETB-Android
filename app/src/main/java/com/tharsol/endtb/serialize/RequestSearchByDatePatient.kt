package com.tharsol.endtb.serialize
import com.google.gson.annotations.SerializedName

data class RequestSearchByDatePatient (
    @field:SerializedName("StartDate")
    val StartDate: String = "",

    @field:SerializedName("EndDate")
    val EndDate: String = ""

)