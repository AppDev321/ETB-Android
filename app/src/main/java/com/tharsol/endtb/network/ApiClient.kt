package com.tharsol.endtb.network

import com.tharsol.endtb.deserialize.*
import com.tharsol.endtb.serialize.RequestChangePassword
import com.tharsol.endtb.serialize.RequestForgot
import com.tharsol.endtb.serialize.RequestLogin
import com.tharsol.endtb.serialize.RequestRegister
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @POST("api/Account/register")
    suspend fun register(@Body body: RequestRegister): Response<LoginResponse>

    @POST("api/Account/login")
    suspend fun login(@Body body: RequestLogin): Response<LoginResponse>

    @POST("api/Account/forgotpassword")
    suspend fun reset(@Body body: RequestForgot): Response<BaseResponse>

    @POST("api/Account/changepassword")
    suspend fun changePassword(@Body body: RequestChangePassword): Response<BaseResponse>

    @GET("api/Patient/FindPatient")
    suspend fun searchPatient(@Query("mobileNumber") mobileNumber: String): Response<FindPatientResponse>

    @GET(ServiceUrls.GET_PRODUCTS)
    suspend fun getProducts(): Response<ProductsResponse>

    @GET(ServiceUrls.GET_LOCALITIES)
    suspend fun getLocalities(): Response<LocalityResponse>

    @GET(ServiceUrls.GET_GENDERS)
    suspend fun getGender(): Response<GendersResponse>

    @POST("api/Transaction/AddPatientTransaction")
    suspend fun addPatientTransaction(@Body body: Patient): Response<BaseResponse>

    @GET("api/Product/GetProductDashboard")
    suspend fun getDashProducts(
        @Query("date") date: String,
        @Header("Cache-Control") cacheControl: String? = null
    ): Response<StockProductsResponse>


}