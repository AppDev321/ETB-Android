package com.tharsol.endtb.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationDataModel ( val Id: String, val date: String, val pharmacy: String,
                                   val patientName: String, var districit: String, var contactNo: String,
                                   val age: String, val image: String,): Parcelable {

                                   }