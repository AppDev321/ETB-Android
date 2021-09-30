package com.tharsol.endtb.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tharsol.endtb.database.doa.GendersDoa
import com.tharsol.endtb.database.doa.LocalityDoa
import com.tharsol.endtb.database.doa.ProductsDoa
import com.tharsol.endtb.database.entity.GenderEntity
import com.tharsol.endtb.database.entity.LocalityEntity
import com.tharsol.endtb.database.entity.ProductEntity
import com.tharsol.endtb.util.App

@Database(entities = [LocalityEntity::class, GenderEntity::class, ProductEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun products(): ProductsDoa
    abstract fun localities(): LocalityDoa
    abstract fun genders(): GendersDoa


    companion object {

        private var instance: AppDb? = null
        const val DATABASE_NAME = "data1.sqlite"

        @JvmStatic
        @Synchronized
        fun get(): AppDb {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    App.context,
                    AppDb::class.java, DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }

        @JvmStatic
        fun setNull() {
            instance = null
        }
    }
}