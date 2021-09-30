package com.tharsol.endtb.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = GenderEntity.TABLE_NAME)
@Parcelize
data class GenderEntity(

    @ColumnInfo(name = COLUMN_ID)
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = COLUMN_LOOKUP_CATEGORY_ID)
    val lookupCategoryId: String? = null,

    @ColumnInfo(name = COLUMN_GENDER_ID)
    val genderId: Int,

    @ColumnInfo(name = COLUMN_NAME)
    val name: String? = null

) : Parcelable {
    companion object {
        const val TABLE_NAME = "Gender"
        const val COLUMN_ID = "_Id"
        const val COLUMN_GENDER_ID = "Id"
        const val COLUMN_LOOKUP_CATEGORY_ID = "LookupCategoryId"
        const val COLUMN_NAME = "Name"

    }
}
