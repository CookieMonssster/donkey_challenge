package com.example.donkeychallenge.extension

import com.example.donkeychallenge.main.MainViewModel.Companion.LOCATION_STRING_FORMAT
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert
import org.junit.Test

class LatLngTest {

    @Test
    fun `Check mapping LatLng object to string correctly`() {
        val latLng = LatLng(10.33, 11.13)
        val result = latLng.toRequestString(LOCATION_STRING_FORMAT)
        Assert.assertEquals(result, "10.33,11.13")
    }
}
