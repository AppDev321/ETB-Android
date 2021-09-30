package com.tharsol.endtb.deserialize

import kotlinx.android.parcel.Parcelize

@Parcelize
class StockProductsResponse : BaseResponse() {
    val data: List<StockProductItem>? = null
}