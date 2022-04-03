package com.example.networktest

import retrofit2.Call
import retrofit2.http.GET

interface AppService {
    @GET("get_data.json")
    fun getAddData(): Call<List<App>>
}