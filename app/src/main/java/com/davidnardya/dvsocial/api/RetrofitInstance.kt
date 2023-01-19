package com.davidnardya.dvsocial.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

inline fun <reified T> createRetrofitInstance(url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build()
    return retrofit.create(T::class.java)
}