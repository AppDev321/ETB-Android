package com.tharsol.endtb.deserialize

import com.google.gson.annotations.SerializedName

data class StockProductItem(

	@field:SerializedName("TypeId")
	val typeId: Int? = null,

	@field:SerializedName("TypeName")
	val typeName: String? = null,

	@field:SerializedName("MonthSaleUnit")
	val monthSaleUnit: Int? = null,

	@field:SerializedName("Description")
	val description: String? = null,

	@field:SerializedName("ProductCode")
	val productCode: String? = null,

	@field:SerializedName("DeviceId")
	val deviceId: String? = null,

	@field:SerializedName("StockQuantity")
	val stockQuantity: Int? = null,

	@field:SerializedName("PharmaCompanyId")
	val pharmaCompanyId: Int? = null,

	@field:SerializedName("TodaySaleUnit")
	val todaySaleUnit: Int? = null,

	@field:SerializedName("Image")
	val image: String? = null,

	@field:SerializedName("Name")
	val name: String? = null,

	@field:SerializedName("PackSize")
	val packSize: String? = null,

	@field:SerializedName("AddedDate")
	val addedDate: String? = null,

	@field:SerializedName("PharmaCompanyName")
	val pharmaCompanyName: String? = null,

	@field:SerializedName("Id")
	val id: Int? = null,

	@field:SerializedName("AddedBy")
	val addedBy: String? = null
)
