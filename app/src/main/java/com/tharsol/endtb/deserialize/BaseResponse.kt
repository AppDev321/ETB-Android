package com.tharsol.endtb.deserialize

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseResponse : Parcelable {

    @field:SerializedName("id")
    val id: Int? = null

    @field:SerializedName("totalCount")
    val totalCount: Int? = null

    @field:SerializedName("activityInfo")
    val activityInfo: String? = null

    @field:SerializedName("successFlag")
    val successFlag: Boolean? = null
}
