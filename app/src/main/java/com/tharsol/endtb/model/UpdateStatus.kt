package com.tharsol.endtb.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Nasser on 12/24/2018.
 */
class UpdateStatus {
    @SerializedName("Urls")
    var urls: MutableList<String>? = null
}