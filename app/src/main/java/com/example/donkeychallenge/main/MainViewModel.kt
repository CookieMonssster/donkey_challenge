package com.example.donkeychallenge.main

import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var klop = "klop"

    fun some() {
        Log.e("klop", "Some: $klop")
    }

    fun someChange(change: String) {
        klop = change
    }
}
