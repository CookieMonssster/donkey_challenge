package com.example.donkeychallenge.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donkeychallenge.api.DonkeyRepository
import com.example.donkeychallenge.extension.toRequestString
import com.example.donkeychallenge.model.HubLocation
import com.example.donkeychallenge.model.SearchResult
import com.example.donkeychallenge.utils.Event
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class MainViewModel(private val donkeyRepository: DonkeyRepository) : ViewModel() {

    private val _hubsLocation = MutableLiveData<List<HubLocation>>()
    val hubsLocations: LiveData<List<HubLocation>> get() = _hubsLocation
    private val _searchResult = MutableLiveData<Event<List<SearchResult>>>()
    val searchResult: LiveData<Event<List<SearchResult>>> get() = _searchResult
    private val _pickedHub = MutableLiveData<Event<HubLocation>>()
    val pickedHub: LiveData<Event<HubLocation>> get() = _pickedHub
    private val _error = MutableLiveData<Event<Throwable>>()
    val error: LiveData<Event<Throwable>> get() = _error

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _error.value = Event(throwable)
    }

    fun getNearbyHubs(latLng: LatLng, radius: Int, currentlyAddedHubsLocations: List<LatLng>) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val newHubs = donkeyRepository.getNearbyHubsLocation(latLng.toRequestString(LOCATION_STRING_FORMAT), radius)
                .map { HubLocation(it.name, LatLng(it.latitude.toDouble(), it.longitude.toDouble())) }
                .toMutableList()

            newHubs.removeAll { currentlyAddedHubsLocations.contains(it.location) }
            _hubsLocation.value = newHubs
        }
    }

    fun search(query: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _searchResult.value = Event(donkeyRepository.search(query))
        }
    }

    fun pickHub(result: SearchResult) = with(result) {
        _pickedHub.value = Event(HubLocation(name, LatLng(latitude.toDouble(), longitude.toDouble())))
    }

    companion object {

        @VisibleForTesting
        const val LOCATION_STRING_FORMAT = "%s,%s"
    }
}
