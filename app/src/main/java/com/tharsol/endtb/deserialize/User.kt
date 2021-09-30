package com.tharsol.endtb.deserialize

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(

	@field:SerializedName("MobileNumber")
	val mobileNumber: String? = null,

	@field:SerializedName("Username")
	val username: String? = null,

	@field:SerializedName("UserId")
	val userId: String? = null,

	@field:SerializedName("Token")
	val token: String? = null,

	@field:SerializedName("ChemistName")
	val chemistName: String? = null,

	@field:SerializedName("ChemistId")
	val chemistId: String? = null
) : Parcelable
