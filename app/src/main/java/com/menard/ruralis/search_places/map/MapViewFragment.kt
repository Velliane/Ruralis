package com.menard.ruralis.search_places.map

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.menard.ruralis.BaseFragment
import com.menard.ruralis.R
import com.menard.ruralis.details.DetailsActivity
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import com.menard.ruralis.utils.setMarker

class MapViewFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnInfoWindowClickListener, GoogleMap.OnCameraIdleListener {

    companion object {
        fun newInstance(): MapViewFragment {
            return MapViewFragment()
        }
    }

    /** GoogleMap */
    private var googleMap: GoogleMap? = null
    private lateinit var viewModel: MapViewModel

    /** Shared Preferences */
    private lateinit var sharedPreferences: SharedPreferences

    /** Radius */
    private var radius: String? = ""

    /** Add fromMaps */
    private var fromMaps: Boolean = true
    private lateinit var noInternet: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map_view, container, false)
        setHasOptionsMenu(true)
        Places.initialize(requireActivity(), context!!.getString(R.string.api_key_google))
        sharedPreferences =
            activity!!.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        fromMaps = sharedPreferences.getBoolean(Constants.PREF_SEARCH_FROM_MAPS, true)
        radius = sharedPreferences.getString(Constants.PREF_SEARCH_AROUND, "50")
        noInternet = view.findViewById(R.id.list_no_internet)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelFactory = Injection.provideViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment)
        (mapFragment as SupportMapFragment).getMapAsync(this)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.toolbar_menu_filter).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    /**
     * Show the GoogleMap
     */
    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true
        googleMap?.setOnMarkerClickListener(this)
        googleMap?.setOnInfoWindowClickListener(this)
        googleMap?.setOnCameraIdleListener(this)
        googleMap?.setInfoWindowAdapter(MarkerAdapter(layoutInflater, requireContext()))
        //-- Check if permissions are granted for FINE_LOCATION or request it --//
        if (checkPermissions()) {
            googleMap?.isMyLocationEnabled = true
            centerMapToUserLocation()
        }
    }

    private fun centerMapToUserLocation() {
        viewModel.locationLiveData.observe(this, Observer {
            if (it != null) {
                val latLng = LatLng(it.latitude, it.longitude)
                googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))

                viewModel.getAllPlaces(
                    "${it.latitude}, ${it.longitude}",
                    radius + "000",
                    "producteur",
                    context!!.resources.getString(R.string.api_key_google),
                    it
                )
                showPlaces()
            }
        })
    }


    private fun showPlaces() {

        viewModel.allPlaceLiveData.observe(this, Observer {
            for (place in it) {
                val latLng = LatLng((place.latitude)!!.toDouble(), (place.longitude)!!.toDouble())
                setMarker(place, googleMap!!, latLng, place.type)
            }
        })
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return false
    }

    override fun onInfoWindowClick(marker: Marker?) {
        val tag = marker?.tag as MarkerTag
        val intent = Intent(context, DetailsActivity::class.java).apply {
            putExtra(Constants.INTENT_ID, tag.id)
            putExtra(Constants.INTENT_FROM, tag.fromRuralis)
            putExtra(Constants.INTENT_URI, tag.photoUri)
        }
        startActivity(intent)
    }

    override fun onCameraIdle() {
        val centerMap: LatLng = googleMap?.cameraPosition!!.target
        val location = Location("")
        location.latitude = centerMap.latitude
        location.longitude = centerMap.longitude
        viewModel.refreshList(
            location,
            radius + "000",
            "producteur",
            context!!.resources.getString(R.string.api_key_google),
            "${centerMap.latitude}, ${centerMap.longitude}"
        )
        viewModel.allPlaceLiveData.observe(this, Observer {
            for (place in it) {
                val latLng = LatLng((place.latitude)!!.toDouble(), (place.longitude)!!.toDouble())
                setMarker(place, googleMap!!, latLng, place.type)
            }
        })
    }
}