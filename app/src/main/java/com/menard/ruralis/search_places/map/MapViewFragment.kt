package com.menard.ruralis.search_places.map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.menard.ruralis.R
import com.menard.ruralis.BaseFragment
import com.menard.ruralis.search_places.MainViewModel
import com.menard.ruralis.utils.Injection

class MapViewFragment : BaseFragment(), OnMapReadyCallback {

    companion object {
        fun newInstance(): MapViewFragment {
            return MapViewFragment()
        }
    }

    /** GoogleMap */
    private var googleMap: GoogleMap? = null
    /** FusedLocation */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_view, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        Places.initialize(requireActivity(), context!!.getString(R.string.api_key_google))

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fragment = childFragmentManager.findFragmentById(R.id.map_fragment)
        (fragment as SupportMapFragment).getMapAsync(this)
    }

    /**
     * Show the GoogleMap
     */
    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true

        //-- Check if permissions are granted for FINE_LOCATION or request it --//
        if (checkPermissions()) {
            googleMap?.isMyLocationEnabled = true
            centerMapToUserLocation()
        }
    }

    private fun centerMapToUserLocation() {
        //-- Create a LocationRequest --//
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        locationRequest.smallestDisplacement = 50F
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY


        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {

                override fun onLocationResult(locationResult: LocationResult?) {
                    val latitude = locationResult!!.lastLocation.latitude
                    val longitude = locationResult.lastLocation.longitude
                    val lastLocation = LatLng(latitude, longitude)

                    googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 12F))
                    showPlaces(latitude.toString(), longitude.toString())
                }
            },
            null
        )
    }

    private fun showPlaces(latitude: String, longitude: String) {

        viewModel.getAllPlaces("$latitude, $longitude", "5000", "maraîcher", context!!.resources.getString(R.string.api_key_google))
        viewModel.allPlaceLiveData.observe(this, Observer {
            for (place in it) {
                val latLng = LatLng((place.latitude)!!.toDouble(), (place.longitude)!!.toDouble())
                if(!place.fromRuralis){
                val markerOptions = MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(place.name)
                googleMap!!.addMarker(markerOptions)
                }else{
                    val markerOptions = MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(place.name+"From Ruralis")
                    googleMap!!.addMarker(markerOptions)
                }
            }
        })
//        viewModel.getTextSearch("$latitude, $longitude", "5000", "maraîcher", context!!.resources.getString(R.string.api_key_google))
//        viewModel.placeTextSearchListLiveData.observe(this, Observer<List<PlaceForList>> {
//            for (place in it) {
//                val latLng = LatLng((place.latitude)!!.toDouble(), (place.longitude)!!.toDouble())
//                val markerOptions = MarkerOptions().position(latLng).title(place.name)
//                googleMap!!.addMarker(markerOptions)
//            }
//        })
    }
}