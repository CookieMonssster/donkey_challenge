package com.example.donkeychallenge.extension

import com.google.android.gms.maps.GoogleMap
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun GoogleMap.cameraMove(): Flow<Unit> = callbackFlow {
    setOnCameraMoveListener { offer(Unit) }
    awaitClose { setOnCameraMoveListener(null) }
}
