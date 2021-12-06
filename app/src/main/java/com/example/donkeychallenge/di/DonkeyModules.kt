package com.example.donkeychallenge.di

import com.example.donkeychallenge.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val donkeyModule = module {
    viewModel { MainViewModel() }
}
