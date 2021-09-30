package com.tharsol.endtb.deserialize

import kotlinx.android.parcel.Parcelize

@Parcelize
class ProductsResponse : BaseResponse() {
    val data: List<Product>? = null
}