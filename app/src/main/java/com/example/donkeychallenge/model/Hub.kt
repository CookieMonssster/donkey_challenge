package com.example.donkeychallenge.model

import com.google.gson.annotations.SerializedName

data class Hub(
    @SerializedName("hub_type")
    val type: String = "type",
    val id: String = "id",
    @SerializedName("account_id")
    val accountId: Int = 3,
    val name: String,
    val latitude: String,
    val longitude: String,
    val radius: Int = 500,
    @SerializedName("available_vehicles_count")
    val availableVehiclesCount: Int = 13,
    @SerializedName("vehicles_count")
    val vehiclesCount: Int = 3,
    @SerializedName("maximum_capacity")
    val maximumCapacity: Int = 3
)
