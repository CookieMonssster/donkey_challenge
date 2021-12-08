package com.example.donkeychallenge.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
    val id: Int,
    val name: String,
    val latitude: String,
    val longitude: String,
    val radius: Int,
    @SerializedName("bikes_count")
    val bikesCount: Int,
    @SerializedName("optimal_capacity")
    val optimalCapacity: Int,
    @SerializedName("maximum_capacity")
    val maximumCapacity: Int,
    val state: String
)
