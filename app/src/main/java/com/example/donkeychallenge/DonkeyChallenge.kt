package com.example.donkeychallenge

import android.app.Application
import com.example.donkeychallenge.di.donkeyModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DonkeyChallenge : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@DonkeyChallenge)
            modules(
                listOf(donkeyModule)
            )
        }
    }
}
