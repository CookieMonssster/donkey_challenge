package com.example.donkeychallenge.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.donkeychallenge.api.DonkeyRepository
import com.example.donkeychallenge.model.NearbyHubs
import com.example.donkeychallenge.model.SearchResult
import com.example.donkeychallenge.utils.Event
import com.google.android.gms.maps.model.LatLng

class MainViewModel(private val donkeyRepository: DonkeyRepository) : ViewModel() {

    private val _hubs = MutableLiveData<NearbyHubs>()
    val hubs: LiveData<NearbyHubs> get() = _hubs
    private val _searchResult = MutableLiveData<Event<List<SearchResult>>>()
    val searchResult: LiveData<Event<List<SearchResult>>> get() = _searchResult
    private val _pickedHub = MutableLiveData<Event<SearchResult>>()
    val pickedHub: LiveData<Event<SearchResult>> get() = _pickedHub

    suspend fun getNearbyHubs(latlng: LatLng) {
        //TODO klop map objects for map use
        _hubs.value = donkeyRepository.getNearbyHubs(locationToString(latlng), 100)
    }

    suspend fun search(query: String) {
        _searchResult.value = donkeyRepository.search(query)
    }

    fun pickHub(result: SearchResult) {
        _pickedHub.value = Event(result)
    }

    private fun locationToString(latlng: LatLng): String = latlng.run {
        String.format(LOCATION_STRING_FORMAT, latlng.latitude, longitude)
    }

    companion object {

        private const val LOCATION_STRING_FORMAT = "%s,%s"
    }
}
