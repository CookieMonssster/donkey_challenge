package com.example.donkeychallenge.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.donkeychallenge.api.DonkeyRepository

class MainViewModel(private val donkeyRepository: DonkeyRepository) : ViewModel() {

    suspend fun getNearbyHubs() {
        val response = donkeyRepository.getNearbyHubs("55.672,12.566", 50)

        Log.e("klop", "H U B S")
        response.hubs.forEach {
            Log.e("klop", "${it.name} ${it.accountId}")
        }
    }

    suspend fun search() {
        val response = donkeyRepository.search("gade")

        Log.e("klop", "S E A R C H   R E S U L T")
        response.forEach {
            Log.e("klop", "${it.name}")
        }
    }
}
