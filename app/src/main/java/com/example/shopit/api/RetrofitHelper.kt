package com.example.shopit.api

import com.example.shopit.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
    }




    val ecommerceApi: EcommerceApi by lazy {
        retrofit.addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EcommerceApi::class.java)
    }
}