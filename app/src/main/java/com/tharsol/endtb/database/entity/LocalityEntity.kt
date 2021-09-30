package com.tharsol.endtb.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = LocalityEntity.TABLE_NAME)
@Parcelize
data class LocalityEntity(

    @ColumnInfo(name = COLUMN_ID)
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = COLUMN_DISTRICT_NAME)
    val districtName: String? = null,

    @ColumnInfo(name = COLUMN_DISTRICT_ID)
    val districtId: Int? = null,

    @ColumnInfo(name = COLUMN_LOCALITY_NAME)
    val localityName: String? = null,

    @ColumnInfo(name = COLUMN_LOCALITY_ID)
    val localityId: Int? = null

) : Parcelable {
    companion object {
        const val TABLE_NAME = "Locality"
        const val COLUMN_ID = "Id"
        const val COLUMN_DISTRICT_ID = "DistrictId"
        const val COLUMN_DISTRICT_NAME = "DistrictName"
        const val COLUMN_LOCALITY_NAME = "LocalityName"
        const val COLUMN_LOCALITY_ID = "LocalityId"

    }
}
