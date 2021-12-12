package com.example.donkeychallenge.extension

import com.google.android.gms.maps.model.LatLng

fun LatLng.toRequestString(format: String) = String.format(format, latitude, longitude)
