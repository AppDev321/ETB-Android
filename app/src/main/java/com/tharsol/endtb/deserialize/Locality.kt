package com.tharsol.endtb.deserialize

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.tharsol.endtb.database.entity.LocalityEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Locality(

	@ColumnInfo(name = LocalityEntity.COLUMN_DISTRICT_NAME)
	@field:SerializedName("DistrictName")
	val districtName: String? = null,

	@ColumnInfo(name = LocalityEntity.COLUMN_DISTRICT_ID)
	@field:SerializedName("DistrictId")
	val districtId: Int? = null,

	@ColumnInfo(name = LocalityEntity.COLUMN_LOCALITY_NAME)
	@field:SerializedName("LocalityName")
	val localityName: String? = null,

	@ColumnInfo(name = LocalityEntity.COLUMN_LOCALITY_ID)
	@field:SerializedName("LocalityId")
	val localityId: Int? = null

) : Parcelable
