package com.menard.ruralis.search_places.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.menard.ruralis.R
import com.menard.ruralis.details.DetailsActivity
import com.menard.ruralis.search_places.ListAdapter
import com.menard.ruralis.search_places.textsearch_model.Result
import com.menard.ruralis.search_places.MainViewModel
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection

class ListViewFragment : Fragment(),
    ListAdapter.OnItemClickListener {


    /** FusedLocation */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    /** Places Client */
    private lateinit var placesClient: PlacesClient
    /** RecyclerView Adapter */
    private lateinit var adapter: ListAdapter
    private lateinit var listResult: List<Result>
    /** Shared Preferences */
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance(): ListViewFragment {
            return ListViewFragment()
        }
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_view, container, false)


        sharedPreferences =
            activity!!.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        //-- configure Recycler View --//
        listResult = ArrayList()
        recyclerView = view.findViewById(R.id.fragment_list_recycler_view)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.HORIZONTAL
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter =
            ListAdapter(this, requireContext())

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        //-- Places SDK initialisation --//
        Places.initialize(requireActivity(), context!!.resources.getString(R.string.api_key_google))
        placesClient = Places.createClient(requireActivity())

        val viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        //getListOfPlaces()
        //getPlacesFromFirestore()
        getUserLocation()

        return view
    }


    private fun getUserLocation() {
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
                    getAllPlaces("$latitude,$longitude")
                    //getListOfPlacesFromRuralis()
                    //getPlacesFromFirestore()
                }
            },
            null
        )
    }


    private fun getAllPlaces(location: String) {
//        viewModel.getAllPlaces(location, "10000", "producteur", context!!.resources.getString(R.string.api_key_google)
//        )
//        viewModel.allPlaceLiveData.observe(this, Observer {
//            if (it.isNotEmpty()) {
//                recyclerView.adapter = adapter
//                adapter.setData(it)
//                adapter.notifyDataSetChanged()
//            }
//        })
        val listOfAll = ArrayList<PlaceForList>()
        viewModel.getAllPlacesFromFirestore()
        viewModel.placeListLiveData.observe(this, Observer {fromFirestore ->
            listOfAll.addAll(fromFirestore)
            viewModel.getTextSearch(location, "10000", "producteur", context!!.resources.getString(R.string.api_key_google))
            viewModel.placeTextSearchListLiveData.observe(this, Observer {fromMaps ->
                listOfAll.addAll(fromMaps)

                if (listOfAll.isNotEmpty()) {
                    recyclerView.adapter = adapter
                    adapter.setData(listOfAll)
                    adapter.notifyDataSetChanged()
                }
            })
        })
    }

    /**
     * Get only Places from Firestore Database
     */
    private fun getPlacesFromFirestore() {
        viewModel.getAllPlacesFromFirestore()
            viewModel.placeListLiveData.observe(this, Observer {
            if (it.isNotEmpty()) {
                recyclerView.adapter = adapter
                adapter.setData(it)
                adapter.notifyDataSetChanged()
            }
        })
    }

    /**
     * Get Places from TextSearch API
     */
    private fun getListOfPlacesFromRuralis() {
        viewModel.getTextSearch(
            "46.6379969,5.234819",
            "10000",
            "producteur",
            context!!.resources.getString(R.string.api_key_google)
        )
        viewModel.placeTextSearchListLiveData.observe(this, Observer<List<PlaceForList>> {
            if (it.isNotEmpty()) {
                recyclerView.adapter = adapter
                adapter.setData(it)
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onItemClicked(id: String, from: Boolean) {
        sharedPreferences.edit().putString(Constants.PREF_ID_PLACE, id).apply()
        val intent = Intent(context, DetailsActivity::class.java).apply {
            putExtra(Constants.INTENT_ID, id)
            putExtra(Constants.INTENT_FROM, from)
        }
        startActivity(intent)
    }
}