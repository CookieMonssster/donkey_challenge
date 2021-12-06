package com.example.donkeychallenge.model

data class SearchResult(
    val id: Int,
    val name: String,
    val latitude: String,
    val longitude: String,
    val radius: Int,
    val bikes_count: Int,
    val optimal_capacity: Int,
    val maximum_capacity: Int,
    val state: String
)
