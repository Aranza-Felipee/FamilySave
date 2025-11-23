package com.example.parcial2.core


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    fun getRetrofit(): Retrofit{

        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")//En esta linea se cambia la url para conectarse a la api
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}