package com.example.donkeychallenge.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.donkeychallenge.api.DonkeyRepository
import com.example.donkeychallenge.model.Hub
import com.example.donkeychallenge.model.HubLocation
import com.example.donkeychallenge.model.SearchResult
import com.example.donkeychallenge.utils.Event
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class MainViewModelTest {

    private val donkeyRepository: DonkeyRepository = mock()
    private val viewModel = MainViewModel(donkeyRepository)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Check getting nearby Hubs method` () = runBlocking {

            val someHubLocations = listOf(
                HubLocation("SomeHub", LatLng(13.31, 12.21))
            )

            val someHub = listOf(
                Hub(name = "SomeHub", latitude = "13.31", longitude = "12.21"),
                Hub(name = "SomeHub2", latitude = "15.51", longitude = "14.41")
            )

            whenever(donkeyRepository.getNearbyHubsLocation(any(), any())).thenReturn(someHub)

            viewModel.getNearbyHubs(LatLng(1.3, 1.3), 100, listOf(LatLng(15.51, 14.41)))
            viewModel.hubsLocations.observeForever { }
            assertEquals(someHubLocations, viewModel.hubsLocations.value)
    }

    @Test
    fun `Check search method`() = runBlocking {

        val someSearchResult = listOf(
            SearchResult(1, "someHubName", "2.33", "3.22"),
            SearchResult(2, "someHubName2", "2.34", "3.23")
        )

        whenever(donkeyRepository.search(any())).thenReturn(someSearchResult)
        viewModel.search("")
        viewModel.searchResult.observeForever {  }
        assertEquals(someSearchResult, viewModel.searchResult.value?.peekContent())
    }

    @Test
    fun `Check picking hub method`() {

        val someSearch = SearchResult(1, "someHubName", "2.33", "3.22")
        val someHub = Event(HubLocation("someHubName", LatLng(2.33, 3.22)))

            viewModel.pickHub(someSearch)
            viewModel.pickedHub.observeForever { }
            assertEquals(viewModel.pickedHub.value?.peekContent(), someHub.peekContent())
    }
}
