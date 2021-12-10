package com.example.donkeychallenge.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.donkeychallenge.api.DonkeyRepository
import com.example.donkeychallenge.model.HubLocation
import com.example.donkeychallenge.model.SearchResult
import com.example.donkeychallenge.utils.Event
import com.google.android.gms.maps.model.LatLng

class MainViewModel(private val donkeyRepository: DonkeyRepository) : ViewModel() {

    private val _hubsLocation = MutableLiveData<List<HubLocation>>()
    val hubsLocations: LiveData<List<HubLocation>> get() = _hubsLocation
    private val _searchResult = MutableLiveData<Event<List<SearchResult>>>()
    val searchResult: LiveData<Event<List<SearchResult>>> get() = _searchResult
    private val _pickedHub = MutableLiveData<Event<HubLocation>>()
    val pickedHub: LiveData<Event<HubLocation>> get() = _pickedHub

    suspend fun getNearbyHubs(latlng: LatLng, radius: Int, currentlyAddedHubsLocations: List<LatLng>) {
        val newHubs = donkeyRepository.getNearbyHubsLocation(locationToString(latlng), radius)
            .map { HubLocation(it.name, LatLng(it.latitude.toDouble(), it.longitude.toDouble())) }
            .toMutableList()

        newHubs.removeAll { currentlyAddedHubsLocations.contains(it.location) }
        _hubsLocation.value = newHubs
    }

    suspend fun search(query: String) {
        _searchResult.value = Event(donkeyRepository.search(query))
    }

    fun pickHub(result: SearchResult) = with(result) {
        _pickedHub.value = Event(HubLocation(name, LatLng(latitude.toDouble(), longitude.toDouble())))
    }

    @VisibleForTesting
    fun locationToString(latLng: LatLng): String = latLng.run {
        String.format(LOCATION_STRING_FORMAT, latLng.latitude, longitude)
    }

    companion object {

        private const val LOCATION_STRING_FORMAT = "%s,%s"
    }
}
