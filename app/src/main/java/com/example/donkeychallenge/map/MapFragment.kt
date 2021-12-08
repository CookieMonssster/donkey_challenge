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
import com.example.donkeychallenge.main.MainViewModel
import com.example.donkeychallenge.search.SearchFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private val viewModel by sharedViewModel<MainViewModel>()

    private val callback = OnMapReadyCallback { googleMap ->
        prepareLiveDataObservers(googleMap)
        //TODO add startin position
        val copenhagen = LatLng(55.678, 12.589)
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(copenhagen, 13f))
        googleMap.setOnCameraMoveListener {
            //TODO klop debounce calls
            getHubsInLocation(googleMap)
        }
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
        initView()
        mapFragment?.getMapAsync(callback)
    }

    private fun prepareLiveDataObservers(map: GoogleMap) = with(map) {
        viewModel.pickedHub.observe(viewLifecycleOwner, {
            LatLng(it.latitude.toDouble(), it.longitude.toDouble()).let { hubLocation ->
                addMarker(MarkerOptions().position(hubLocation).title(it.name))
                animateCamera(CameraUpdateFactory.newLatLngZoom(hubLocation, 15f))
            }
        })

        viewModel.hubs.observe(viewLifecycleOwner, {
            Log.e("klop", "H U B S")
            it.hubs.forEach {
                Log.e("klop", "${it.name}")
            }
        })
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

    private fun getHubsInLocation(map: GoogleMap) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getNearbyHubs(map.cameraPosition.target)
        }
    }
}
