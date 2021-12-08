package com.example.donkeychallenge.api

import com.example.donkeychallenge.model.NearbyHubs
import com.example.donkeychallenge.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface DonkeyService {

    @GET("api/public/nearby")
    suspend fun getNearbyHubs(
        @Header(ACCEPT) accept: String = ACCEPT_NEARBY,
        @Query(FILTER_TYPE) filterType: String,
        @Query(LOCATION) location: String,
        @Query(RADIUS) radius: Int
    ): NearbyHubs

    @GET("api/owners/admins/{$ADMIN_ID}/hubs/search")
    suspend fun search(
        @Header(ACCEPT) accept: String = ACCEPT_SEARCH,
        @Path(ADMIN_ID) adminId: Int = DEFAULT_ADMIN_ID,
        @Query(QUERY) query: String
    ) : List<SearchResult>

    companion object {

        private const val ACCEPT = "Accept"
        private const val FILTER_TYPE = "filter_type"
        private const val ADMIN_ID = "admin_id"
        private const val ACCEPT_NEARBY = "application/com.donkeyrepublic.v7"
        private const val ACCEPT_SEARCH = "application/com.donkeyrepublic.v2"
        private const val LOCATION = "location"
        private const val RADIUS = "radius"
        private const val DEFAULT_ADMIN_ID = 1090
        private const val QUERY = "query"
    }
}
