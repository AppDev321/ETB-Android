package com.tharsol.endtb.model

import android.os.Parcelable
import com.tharsol.endtb.util.App
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.text.MessageFormat
import java.util.*


@Parcelize
class SingleItem(var value: Int?, var referenceId: String?,
                 var title: String?, var description: String?,
                 var description1: String?, var description2: String?,
                 var brand: Int?, var tag: @RawValue Any? = null, var isSelected: Boolean) : Parcelable {


    constructor() : this(null, null, null, null,
            null, null, null, null, false) {
    }

    constructor(value: Int?, title: String?) : this(value, null, title, null,
            null, null, null, null, false) {
    }

    constructor(value: Int, title: String?, description: String?) : this(value, null, title, description,
            null, null, null, null, false) {
    }

    constructor(referenceId: String?, title: String?) : this(null, referenceId, title, null,
            null, null, null, null, false) {
    }

    constructor(referenceId: String?, title: String?, description: String?) : this(null, referenceId, title, description,
            null, null, null, null, false) {
    }


//    public fun setValue(value: Int) {
//        this.value = value
//    }
//
//    fun getValue(): Int? {
//        return value
//    }


    override fun equals(obj: Any?): Boolean {
        return if (obj is SingleItem) {
            value == obj.value
        } else super.equals(obj)
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

    companion object {


        /**
         * it will convert List of single item in to list
         * of string. It using {!link [SingleItem.getValue]}
         * as string and add into list
         *
         * @param items list contain object of [<]
         * @return list can be empty or contain object of [String]
         */
        fun convertRefToList(items: List<SingleItem>): List<String?> {
            val list: MutableList<String?> = ArrayList()
            for (item in items) {
                list.add(item.referenceId)
            }
            return list
        }
    }
}