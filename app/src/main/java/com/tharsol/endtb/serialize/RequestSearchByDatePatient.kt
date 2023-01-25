package com.tharsol.endtb.serialize
import com.google.gson.annotations.SerializedName

data class RequestSearchByDatePatient (
    @field:SerializedName("StartDate")
    val StartDate: String = "2023-01-25",

    @field:SerializedName("EndDate")
    val EndDate: String = ""

)