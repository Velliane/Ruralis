package com.menard.ruralis.search_places.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.widget.addTextChangedListener
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
import com.menard.ruralis.utils.ConnectionLiveData
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.fragment_list_view.*

class ListViewFragment : Fragment(),
    ListAdapter.OnItemClickListener, View.OnClickListener{


    /** FusedLocation */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    /** Places Client */
    private lateinit var placesClient: PlacesClient
    /** RecyclerView Adapter */
    private lateinit var adapter: ListAdapter
    private lateinit var listResult: List<Result>
    /** Shared Preferences */
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: ListViewModel
    /** Radius */
    private var radius: String? = "80000"
    /** Add fromMaps */
    private var fromMaps: Boolean = true
    private lateinit var textSearch: AppCompatAutoCompleteTextView
    private lateinit var searchBtn: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var noInternet: TextView
    private lateinit var connectionLiveData: ConnectionLiveData

    companion object {
        fun newInstance(): ListViewFragment {
            return ListViewFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_view, container, false)
        setHasOptionsMenu(true)
        bindViews(view)
        //-- Shared Preferences --//
        sharedPreferences = activity!!.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        fromMaps = sharedPreferences.getBoolean(Constants.PREF_SEARCH_FROM_MAPS, true)
        radius = sharedPreferences.getString(Constants.PREF_SEARCH_AROUND, "50000")
        //-- Places SDK initialisation --//
        Places.initialize(requireActivity(), context!!.resources.getString(R.string.api_key_google))
        placesClient = Places.createClient(requireActivity())
        connectionLiveData = ConnectionLiveData(requireContext())
        //-- Location --//
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        //-- Show Places --//
        connectionLiveData.observe(this, Observer {
            if(it.isConnected) {
                Log.d("IsConnected", it.isConnected.toString())
                getUserLocation()
            }else {
                recyclerView.visibility = View.GONE
                noInternet.visibility = View.VISIBLE
            }
        })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelFactory = Injection.provideViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
        viewModel.init()
    }

    private fun bindViews(view: View) {
        listResult = ArrayList()
        recyclerView = view.findViewById(R.id.fragment_list_recycler_view)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL))
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = ListAdapter(this, requireContext())
        recyclerView.adapter = adapter
        noInternet = view.findViewById(R.id.list_no_internet)
        textSearch = view.findViewById(R.id.edit_search)
        textSearch.addTextChangedListener {
            viewModel.filter(it.toString())
            viewModel.filteredPlacesLiveData.observe(this, Observer {list ->
                adapter.setData(list)
            })
        }
        searchBtn = view.findViewById(R.id.custom_search_keyword_btn)
        searchBtn.setOnClickListener(this)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.toolbar_menu_filter).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_menu_filter -> {
                if (activity_main_search.visibility == View.GONE) {
                    activity_main_search.visibility = View.VISIBLE
                }else{
                    activity_main_search.visibility = View.GONE
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUserLocation() {
        //-- Create a LocationRequest --//
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        locationRequest.smallestDisplacement = 50F
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        val latitude = locationResult!!.lastLocation.latitude
                        val longitude = locationResult.lastLocation.longitude
                        //-- Create Location object to get distance between place and user --//
                        val location = Location("")
                        location.latitude = latitude
                        location.longitude = longitude
                        //-- Search places --//
                        viewModel.initViewModel("$latitude,$longitude", radius!!, "producteur", location)
                        if (fromMaps) {
                            showAllPlaces()
                        } else {
                            getPlacesFromFirestore()
                        }
                    }
                }, null
            )
    }

    /**
     * Get places from Firestore and GoogleMaps
     */
    private fun showAllPlaces(){
        viewModel.allPlaceLiveData.observe(this, Observer {
            if (it.isNotEmpty()) {
                adapter.setData(it)
            }
        })
    }

    /**
     * Get only Places from Firestore Database
     */
    private fun getPlacesFromFirestore() {
        viewModel.getOnlyPlaceFromFirestore()
            viewModel.placeListLiveData.observe(this, Observer {
            if (it.isNotEmpty()) {
                adapter.setData(it)
            }
        })
    }


    /**
     * Start DetailsActivity when an item is clicked
     */
    override fun onItemClicked(id: String, from: Boolean, photo: String?) {
        sharedPreferences.edit().putString(Constants.PREF_ID_PLACE, id).apply()
        val intent = Intent(context, DetailsActivity::class.java).apply {
            putExtra(Constants.INTENT_ID, id)
            putExtra(Constants.INTENT_FROM, from)
            putExtra(Constants.INTENT_URI, photo)
        }
        startActivity(intent)
    }

    override fun onClick(view: View?) {
        when(view){
            searchBtn -> {
                viewModel.filter(textSearch.text.toString())
                viewModel.filteredPlacesLiveData.observe(this, Observer {
                    adapter.setData(it)
                })
            }
        }
    }

}