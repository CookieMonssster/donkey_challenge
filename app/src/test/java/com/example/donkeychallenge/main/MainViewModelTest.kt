package com.example.donkeychallenge.main

import com.example.donkeychallenge.api.DonkeyRepository
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    private val donkeyRepository = mock<DonkeyRepository>()
    private val viewModel = MainViewModel(donkeyRepository)

//    @get:Rule
//    var instantExecutorRule = InstantTaskExecutorRule()
//
//    @ExperimentalCoroutinesApi
//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()
//
//    @Before
//    fun setup() {
//        MockitoAnnotations.initMocks(this)
//    }
//
//
//    @Test
//    fun `Check that picking hub works correctly`() {
//
//        val searchResultObserver: Observer<Event<SearchResult>>
//        val searchResult = listOf(SearchResult(0, "name", "1", "2"))
//        every { runBlocking { donkeyRepository.search(any()) } } returns searchResult
//
//
//        runBlocking {
//            viewModel.searchResult.observeForever(searchResultObserver)
//            viewModel.search("some") }
//
//    }

    @Test
    fun `Check mapping LatLng object to string correctly`() {
        val latLng = LatLng(10.33, 11.13)
        val result = viewModel.locationToString(latLng)
        assertEquals(result, "10.33,11.13")
    }
}
