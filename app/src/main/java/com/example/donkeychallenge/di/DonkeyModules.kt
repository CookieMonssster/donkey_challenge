package com.example.donkeychallenge.di

import com.example.donkeychallenge.BuildConfig
import com.example.donkeychallenge.api.DonkeyRepository
import com.example.donkeychallenge.api.DonkeyRepositoryImpl
import com.example.donkeychallenge.api.RetrofitProvider
import com.example.donkeychallenge.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val donkeyModule = module {
    single { RetrofitProvider.provideOkHttpClient() }
    single { RetrofitProvider.provideRetrofit(get(), BuildConfig.DONKEY_API_URL) }
    single<DonkeyRepository> { DonkeyRepositoryImpl(get()) }
    viewModel { MainViewModel(get()) }
}
