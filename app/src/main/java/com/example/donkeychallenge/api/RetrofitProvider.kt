package com.example.donkeychallenge.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    private const val TIMEOUT_TIME: Long = 33

    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
        .build()

    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String
    ): DonkeyService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build().create(DonkeyService::class.java)
}
