package com.example.donkeychallenge.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    private const val TIMEOUT_TIME: Long = 13
    private const val CONTENT_TYPE = "Content-type"
    private const val APPLICATION_JSON = "application/json"
    private const val AUTHORIZATION = "Authorization"
    private const val AUTHORIZATION_TOKEN = "y7X7cWCGj69fCvczQweU"

    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_TIME, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                    .addHeader(AUTHORIZATION, AUTHORIZATION_TOKEN)
                    .build()
            )
        }
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
