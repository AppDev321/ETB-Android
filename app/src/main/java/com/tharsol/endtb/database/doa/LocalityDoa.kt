package com.tharsol.endtb.database.doa

import androidx.room.*
import com.tharsol.endtb.database.entity.LocalityEntity
import com.tharsol.endtb.deserialize.Locality

@Dao
interface LocalityDoa {


    @Insert(entity = LocalityEntity::class, onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(items: List<Locality>): List<Long>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query(
        "select distinct "
            .plus(LocalityEntity.COLUMN_DISTRICT_ID)
                + ", ".plus(LocalityEntity.COLUMN_DISTRICT_NAME)
                + " from ".plus(LocalityEntity.TABLE_NAME)
                + " where " + LocalityEntity.COLUMN_DISTRICT_ID + " > 0"
    )
    fun getDistrict(): List<Locality>?

    @Query(
        "select distinct "
            .plus(LocalityEntity.COLUMN_LOCALITY_ID)
                + ", ".plus(LocalityEntity.COLUMN_LOCALITY_NAME)
                + " from ".plus(LocalityEntity.TABLE_NAME)
            .plus(" where ").plus(LocalityEntity.COLUMN_DISTRICT_ID)
            .plus("= :districtId")
    )
    fun getLocalitys(districtId: Int?): List<Locality>?

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query(
        "select * from ".plus(LocalityEntity.TABLE_NAME)
            .plus(" where ").plus(LocalityEntity.COLUMN_LOCALITY_ID)
            .plus("= :LocalityId")
    )

    fun getLocality(LocalityId: Int?): Locality?


}