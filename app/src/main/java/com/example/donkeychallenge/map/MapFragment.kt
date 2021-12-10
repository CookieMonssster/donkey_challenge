package com.example.donkeychallenge.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.donkeychallenge.R
import com.example.donkeychallenge.databinding.FragmentMapBinding
import com.example.donkeychallenge.extension.cameraMove
import com.example.donkeychallenge.extension.clicks
import com.example.donkeychallenge.extension.isConnected
import com.example.donkeychallenge.main.MainViewModel
import com.example.donkeychallenge.model.HubLocation
import com.example.donkeychallenge.search.SearchFragment
import com.example.donkeychallenge.utils.EventObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private val viewModel by sharedViewModel<MainViewModel>()
    private val markersOnMap = mutableListOf<Marker>()

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COPENHAGEN, INIT_ZOOM))
        prepareLiveDataObservers(googleMap)
        initOnMapMoveListener(googleMap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentMapBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initView()
    }

    private fun prepareLiveDataObservers(googleMap: GoogleMap) = with(googleMap) {
        viewModel.pickedHub.observe(viewLifecycleOwner, EventObserver { showPickedHub(this, it) })
        viewModel.hubsLocations.observe(viewLifecycleOwner, { showHubsLocation(this, it) })
    }

    private fun initOnMapMoveListener(googleMap: GoogleMap) = with(googleMap) {
        lifecycleScope.launch {
            cameraMove().sample(MARKER_REFRESH_INTERVAL).collect { getNearbyHubs(this@with) }
        }
    }

    private fun showPickedHub(googleMap: GoogleMap, hub: HubLocation) = with(googleMap) {
        addMarker(MarkerOptions().position(hub.location).title(hub.name))?.apply {
            showInfoWindow()
            markersOnMap.add(this)
        }
        moveCamera(CameraUpdateFactory.newLatLngZoom(hub.location, INIT_ZOOM))
    }

    private fun showHubsLocation(googleMap: GoogleMap, hubs: List<HubLocation>) = with(googleMap) {
        hubs.forEach {
            if (projection.visibleRegion.latLngBounds.contains(it.location)) {
                addMarker(MarkerOptions().position(it.location).title(it.name))?.let { marker ->
                    markersOnMap.add(marker)
                }
            }
        }
    }

    private suspend fun getNearbyHubs(googleMap: GoogleMap) = with(googleMap) {
        removeInvisibleMarkers(this)
        projection.visibleRegion.latLngBounds.run {
            val radius = (SphericalUtil.computeDistanceBetween(northeast, southwest) / 2).toInt()
            if(requireContext().isConnected()) {
                viewModel.getNearbyHubs(cameraPosition.target, radius, markersOnMap.map { it.position })
            } else Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeInvisibleMarkers(googleMap: GoogleMap) = with(googleMap) {
        val currentBounds = projection.visibleRegion.latLngBounds
        val markersToRemove = mutableListOf<Marker>()
        markersOnMap.forEach {
            if (currentBounds.contains(it.position).not()) {
                markersToRemove.add(it)
                it.remove()
            }
        }
        markersOnMap.removeAll(markersToRemove)
    }

    private fun initView() = with(lifecycleScope) {
        launch {
            binding.searchButton.clicks().collect {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.fragment_container, SearchFragment.newInstance())
                    ?.addToBackStack(null)
                    ?.commit()
            }
        }
    }

    companion object {

        private val COPENHAGEN = LatLng(55.678, 12.589)
        private const val MARKER_REFRESH_INTERVAL = 1000L
        private const val INIT_ZOOM = 15f
    }
}
