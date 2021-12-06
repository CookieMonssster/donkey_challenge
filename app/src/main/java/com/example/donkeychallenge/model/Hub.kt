package com.example.donkeychallenge.model

import com.google.gson.annotations.SerializedName

data class Hub(
    @SerializedName("hub_type")
    val type: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("account_id")
    val accountId: Int,
    val name: String,
    val latitude: String,
    val longitude: String,
    val radius: Int,
    @SerializedName("available_vehicles_count")
    val availableVehiclesCount: Int,
    @SerializedName("vehicles_count")
    val vehiclesCount: Int,
    @SerializedName("maximum_capacity")
    val maximumCapacity: Int
)
