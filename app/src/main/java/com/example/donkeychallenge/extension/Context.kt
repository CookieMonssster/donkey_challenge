package com.example.donkeychallenge.extension

import android.content.Context
import android.net.ConnectivityManager

//TODO add support for 28=<
fun Context.isConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}
