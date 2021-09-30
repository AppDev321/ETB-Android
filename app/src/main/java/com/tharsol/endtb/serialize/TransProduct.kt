package com.tharsol.endtb.serialize

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransProduct(
	@field:SerializedName("PatientId")
	val patientId: Int? = null,

	@field:SerializedName("Quantity")
	val quantity: Int? = null,

	@field:SerializedName("ProductId")
	val productId: Int? = null
) : Parcelable
