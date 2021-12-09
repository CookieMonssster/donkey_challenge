package com.example.donkeychallenge.map

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.donkeychallenge.R
import com.example.donkeychallenge.databinding.FragmentMapBinding
import com.example.donkeychallenge.extension.cameraMove
import com.example.donkeychallenge.main.MainViewModel
import com.example.donkeychallenge.search.SearchFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private val viewModel by sharedViewModel<MainViewModel>()

    private val callback = OnMapReadyCallback { googleMap ->
        //TODO klop save last position
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COPENHAGEN, INIT_ZOOM))
        initOnMapMoveListener(googleMap)
        prepareLiveDataObservers(googleMap)
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
        viewModel.pickedHub.observe(viewLifecycleOwner, {
            it.peekContent().apply {
                LatLng(latitude.toDouble(), longitude.toDouble()).let { hubLocation ->
                    //TODO remove this marker
                    addMarker(MarkerOptions().position(hubLocation).title(it.peekContent().name))
                    animateCamera(CameraUpdateFactory.newLatLngZoom(hubLocation, INIT_ZOOM))
                }
            }
        })

        viewModel.hubs.observe(viewLifecycleOwner, {
            //TODO add showing markers
            //googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))

            Log.e("klop", "H U B S")
            it.hubs.forEach {
                Log.e("klop", "${it.name}")
            }
        })
    }

    private fun initOnMapMoveListener(googleMap: GoogleMap) = with(googleMap) {
        viewLifecycleOwner.lifecycleScope.launch {
            cameraMove().sample(MARKER_REFRESH_INTERVAL)
                .collect { viewModel.getNearbyHubs(cameraPosition.target) }
        }
    }

    private fun initView() {
        binding.searchButton.setOnClickListener {
            //TODO use navigator instead?
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragment_container, SearchFragment.newInstance())
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    companion object {

        private val COPENHAGEN = LatLng(55.678, 12.589)
        private const val MARKER_REFRESH_INTERVAL = 1000L
        private const val INIT_ZOOM = 15f
    }
}
