package com.tharsol.endtb.deserialize

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.tharsol.endtb.database.entity.ProductEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(

	@ColumnInfo(name = ProductEntity.COLUMN_TYPE_ID)
	@field:SerializedName("TypeId")
	val typeId: Int? = null,
	@ColumnInfo(name = ProductEntity.COLUMN_TYPE_NAME)
	@field:SerializedName("TypeName")
	val typeName: String? = null,
	@ColumnInfo(name = ProductEntity.COLUMN_DESCRIPTION)
	@field:SerializedName("Description")
	val description: String? = null,
	@ColumnInfo(name = ProductEntity.COLUMN_PRODUCT_CODE)
	@field:SerializedName("ProductCode")
	val productCode: String? = null,
	@ColumnInfo(name = ProductEntity.COLUMN_PHARMA_COMPANY_ID)
	@field:SerializedName("PharmaCompanyId")
	val pharmaCompanyId: Int? = null,

	@ColumnInfo(name = ProductEntity.COLUMN_NAME)
	@field:SerializedName("Name")
	val name: String? = null,
	@ColumnInfo(name = ProductEntity.COLUMN_PACK_SIZE)
	@field:SerializedName("PackSize")
	val packSize: String? = null,
	@ColumnInfo(name = ProductEntity.COLUMN_PHARMA_COMPANY_NAME)
	@field:SerializedName("PharmaCompanyName")
	val pharmaCompanyName: String? = null,
	@ColumnInfo(name = ProductEntity.COLUMN_ID)
	@field:SerializedName("Id")
	val id: Int? = null
) : Parcelable
