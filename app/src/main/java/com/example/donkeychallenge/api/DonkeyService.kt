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
        @Header(CONTENT_TYPE) content: String = APPLICATION_JSON,
        @Header(ACCEPT) accept: String = ACCEPT_NEARBY,
        @Query(FILTER_TYPE) filterType: String,
        @Query(LOCATION) location: String,
        @Query(RADIUS) radius: Int
    ): NearbyHubs

    @GET("api/owners/admins/{$ADMIN_ID}/hubs/search")
    suspend fun search(
        @Header(CONTENT_TYPE) content: String = APPLICATION_JSON,
        @Header(ACCEPT) accept: String = ACCEPT_SEARCH,
        @Header(AUTHORIZATION) authorization: String = AUTHORIZATION_TOKEN,
        @Path(ADMIN_ID) adminId: Int = DEFAULT_ADMIN_ID,
        @Query(QUERY) query: String
    ) : List<SearchResult>

    companion object {

        private const val CONTENT_TYPE = "Content-type"
        private const val APPLICATION_JSON = "application/json"
        private const val ACCEPT = "Accept"
        private const val AUTHORIZATION = "Authorization"
        private const val FILTER_TYPE = "filter_type"
        private const val ADMIN_ID = "admin_id"
        private const val ACCEPT_NEARBY = "application/com.donkeyrepublic.v7"
        private const val ACCEPT_SEARCH = "application/com.donkeyrepublic.v2"
        private const val LOCATION = "location"
        private const val RADIUS = "radius"
        private const val AUTHORIZATION_TOKEN = "y7X7cWCGj69fCvczQweU" //is it sensitive data?
        private const val DEFAULT_ADMIN_ID = 1090 //is it sensitive data?
        private const val QUERY = "query"
    }
}
