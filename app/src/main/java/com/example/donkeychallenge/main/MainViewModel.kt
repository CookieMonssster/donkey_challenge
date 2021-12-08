package com.example.donkeychallenge.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.donkeychallenge.api.DonkeyRepository
import com.example.donkeychallenge.model.NearbyHubs
import com.example.donkeychallenge.model.SearchResult
import com.google.android.gms.maps.model.LatLng

class MainViewModel(private val donkeyRepository: DonkeyRepository) : ViewModel() {

    private val _hubs = MutableLiveData<NearbyHubs>()
    val hubs: LiveData<NearbyHubs> get() = _hubs
    private val _searchResult = MutableLiveData<List<SearchResult>>()
    val searchResult: LiveData<List<SearchResult>> get() = _searchResult
    private val _pickedHub = MutableLiveData<SearchResult>()
    val pickedHub: LiveData<SearchResult> get() = _pickedHub


    suspend fun getNearbyHubs(latlng: LatLng) {
        _hubs.value = donkeyRepository.getNearbyHubs(locationToString(latlng), 100)
    }

    suspend fun search(query: String) {
        _searchResult.value = donkeyRepository.search(query)
    }

    fun clearSearchResults() {
        _searchResult.value = emptyList()
    }

    fun pickHub(result: SearchResult) {
        _pickedHub.value = result
    }

    private fun locationToString(latlng: LatLng): String = latlng.run {
        String.format(LOCATION_STRING_FORMAT, latlng.latitude, longitude)
    }

    companion object {

        private const val LOCATION_STRING_FORMAT = "%s,%s"
    }
}
