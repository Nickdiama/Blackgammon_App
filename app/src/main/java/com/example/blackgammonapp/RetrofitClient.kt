package com.example.blackgammonapp

import com.example.blackgammonapp.data.BlackgammonApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "https://users.iee.ihu.gr/~iee2019039/"

    val api: BlackgammonApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BlackgammonApi::class.java)
    }

}