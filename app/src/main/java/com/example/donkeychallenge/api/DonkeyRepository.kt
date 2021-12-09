package com.example.donkeychallenge.api

import com.example.donkeychallenge.model.NearbyHubs
import com.example.donkeychallenge.model.SearchResult
import com.example.donkeychallenge.utils.Event

interface DonkeyRepository {

    suspend fun getNearbyHubs(location: String, radius: Int): NearbyHubs

    suspend fun search(query: String): Event<List<SearchResult>>
}

class DonkeyRepositoryImpl(private val service: DonkeyService) : DonkeyRepository {

    override suspend fun getNearbyHubs(location: String, radius: Int): NearbyHubs =
        service.getNearbyHubs(filterType = FILTER_TYPE, location = location, radius = radius)

    override suspend fun search(query: String): Event<List<SearchResult>> = Event(service.search(query = query))

    companion object {

        private const val FILTER_TYPE = "radius"
    }
}
