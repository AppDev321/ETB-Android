package com.tharsol.endtb.serialize

import com.google.gson.annotations.SerializedName

data class RequestRegister(

	@field:SerializedName("MobileNumber")
	val mobileNumber: String? = null,

	@field:SerializedName("Username")
	val username: String? = null,

	@field:SerializedName("LicenceNumber")
	val licenceNumber: String? = null,

	@field:SerializedName("DeviceId")
	val deviceId: String? = null,

	@field:SerializedName("Password")
	val password: String? = null
)
