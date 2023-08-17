package com.tharsol.endtb.deserialize

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tharsol.endtb.serialize.TransProduct
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Patient(

	@field:SerializedName("Address")
	val address: String? = null,

	@field:SerializedName("Cnic")
	val cnic: String? = null,

//	@field:SerializedName("Locality")
//	val locality: Int? = null,

	@field:SerializedName("TreatmentStartDate")
	val treatmentStartDate: String? = null,

	@field:SerializedName("HealthStatus")
	val healthStatus: Int? = null,

	@field:SerializedName("Gender")
	val gender: Int? = null,

	@field:SerializedName("Image")
	var image: String? = null,

	@field:SerializedName("Name")
	val name: String? = null,

	@field:SerializedName("MobileNumber")
	val mobileNumber: String? = null,

	@field:SerializedName("TreatmentEndDate")
	val treatmentEndDate: String? = null,

	@field:SerializedName("TreatmentStatus")
	val treatmentStatus: Int? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("AddedBy")
	var addedBy: String? = null,

	@field:SerializedName("Age")
	val age: Int? = null,

//	@field:SerializedName("Transactions")
//	var transactions: List<TransProduct>? = null,


	) : Parcelable
