package uz.max.anychart.api

import retrofit2.Call
import retrofit2.http.GET
import uz.max.anychart.data.ProductData
import uz.max.anychart.data.UserData
import uz.max.anychart.data.ValueData

interface ChartApi {
    @GET("/analytic/balance")
    fun balance():Call<ResponseData<List<ValueData>>>

    @GET("/analytic/tasks")
    fun tasks():Call<ResponseData<List<ValueData>>>

    @GET("/analytic/users")
    fun users():Call<ResponseData<List<UserData>>>

    @GET("/analytic/workers")
    fun workers():Call<ResponseData<List<ValueData>>>

    @GET("/analytic/products")
    fun products():Call<ResponseData<List<ProductData>>>

    @GET("/analytic/balance/all")
    fun allBalance():Call<ResponseData<List<ValueData>>>

    @GET("/analytic/tasks/all")
    fun allTasks():Call<ResponseData<List<ValueData>>>

    @GET("/analytic/users/all")
    fun allUsers():Call<ResponseData<List<UserData>>>

    @GET("/analytic/workers/all")
    fun allWworkers():Call<ResponseData<List<ValueData>>>

    @GET("/analytic/products/all")
    fun allProducts():Call<ResponseData<List<ProductData>>>
}