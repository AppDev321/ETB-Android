package com.tharsol.endtb.deserialize

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.tharsol.endtb.database.entity.GenderEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gender(

	@ColumnInfo(name = GenderEntity.COLUMN_LOOKUP_CATEGORY_ID)
	@field:SerializedName("LookupCategoryId")
	val lookupCategoryId: String? = null,

	@ColumnInfo(name = GenderEntity.COLUMN_GENDER_ID)
	@field:SerializedName("Id")
	val id: Int? = null,

	@ColumnInfo(name = GenderEntity.COLUMN_NAME)
	@field:SerializedName("Name")
	val name: String? = null
) : Parcelable
