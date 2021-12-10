package com.example.donkeychallenge.api

import com.example.donkeychallenge.model.Hub
import com.example.donkeychallenge.model.SearchResult
import com.example.donkeychallenge.utils.Event

interface DonkeyRepository {

    suspend fun getNearbyHubsLocation(location: String, radius: Int): List<Hub>

    suspend fun search(query: String): List<SearchResult>
}

class DonkeyRepositoryImpl(private val service: DonkeyService) : DonkeyRepository {

    override suspend fun getNearbyHubsLocation(location: String, radius: Int): List<Hub> =
        service.getNearbyHubs(filterType = FILTER_TYPE, location = location, radius = radius).hubs

    override suspend fun search(query: String): List<SearchResult> = service.search(query = query)

    companion object {

        private const val FILTER_TYPE = "radius"
    }
}
