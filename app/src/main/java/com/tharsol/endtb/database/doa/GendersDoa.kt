package com.tharsol.endtb.database.doa

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tharsol.endtb.database.entity.GenderEntity
import com.tharsol.endtb.deserialize.Gender

@Dao
interface GendersDoa {


    @Insert(entity = GenderEntity::class,onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(items: List<Gender>): List<Long>

    @Query("select * from ".plus(GenderEntity.TABLE_NAME))
    fun getGenders(): List<Gender>


}