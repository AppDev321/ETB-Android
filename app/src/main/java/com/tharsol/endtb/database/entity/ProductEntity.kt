package com.tharsol.endtb.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = ProductEntity.TABLE_NAME)
@Parcelize
data class ProductEntity(

	@ColumnInfo(name = "_Id")
	@PrimaryKey(autoGenerate = true)
	val chemistId: Int? = null,

	@ColumnInfo(name = COLUMN_TYPE_ID)
	val typeId: Int? = null,

	@ColumnInfo(name = COLUMN_TYPE_NAME)
	val typeName: String? = null,

	@ColumnInfo(name = COLUMN_DESCRIPTION)
	val description: String? = null,

	@ColumnInfo(name = COLUMN_PRODUCT_CODE)
	val productCode: String? = null,

	@ColumnInfo(name = COLUMN_PHARMA_COMPANY_ID)
	val pharmaCompanyId: Int? = null,

	@ColumnInfo(name = COLUMN_NAME)
	val name: String? = null,

	@ColumnInfo(name = COLUMN_PACK_SIZE)
	val packSize: String? = null,

	@ColumnInfo(name = COLUMN_PHARMA_COMPANY_NAME)
	val pharmaCompanyName: String? = null,

	@ColumnInfo(name = COLUMN_ID)
	val id: Int? = null
) : Parcelable {
    companion object {
        const val TABLE_NAME = "AllProducts"
        const val COLUMN_TYPE_ID = "TypeId"
        const val COLUMN_TYPE_NAME = "TypeName"
        const val COLUMN_DESCRIPTION = "Description"
        const val COLUMN_PRODUCT_CODE = "ProductCode"
        const val COLUMN_PHARMA_COMPANY_ID = "PharmaCompanyId"
        const val COLUMN_NAME = "Name"
        const val COLUMN_PACK_SIZE = "PackSize"
        const val COLUMN_PHARMA_COMPANY_NAME = "PharmaCompanyName"
        const val COLUMN_ID = "Id"

    }
}
