package com.example.parcial2.core


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    fun getRetrofit(): Retrofit{

        return Retrofit.Builder()
            .baseUrl("http://192.168.20.9:3000/")//En esta linea se cambia la url para conectarse a la api
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}