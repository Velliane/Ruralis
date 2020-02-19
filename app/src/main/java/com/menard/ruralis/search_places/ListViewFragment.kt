package com.menard.ruralis.search_places

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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.menard.ruralis.R
import com.menard.ruralis.add_places.Place
import com.menard.ruralis.details.DetailsActivity
import com.menard.ruralis.search_places.textsearch_model.Result
import com.menard.ruralis.search_places.textsearch_model.TextSearch
import com.menard.ruralis.search_places.view_model.PlacesViewModel
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection

class ListViewFragment : Fragment(), ListAdapter.OnItemClickListener {


    /** FusedLocation */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    /** Places Client */
    private lateinit var placesClient: PlacesClient
    /** RecyclerView Adapter */
    private lateinit var adapter: ListAdapter
    private lateinit var listResult: List<Result>
    /** Shared Preferences */
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        fun newInstance(): ListViewFragment {
            return ListViewFragment()
        }
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_view, container, false)


        sharedPreferences = activity!!.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
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
        adapter = ListAdapter(this, requireContext())

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        //-- Places SDK initialisation --//
        Places.initialize(requireActivity(), context!!.resources.getString(R.string.api_key_google))
        placesClient = Places.createClient(requireActivity())

        getListOfPlaces()
        //getPlacesFromFirestore()

        return view
    }

    private fun getPlacesFromFirestore() {
        val viewModelFactory = Injection.providePlacesViewModelFactory()
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlacesViewModel::class.java)
        viewModel.getAllPlacesFromFirestore().observe(this, Observer {
            if(it.isNotEmpty()){
                recyclerView.adapter = adapter
                adapter.setData(it)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun getListOfPlaces() {
        val viewModelFactory = Injection.providePlacesViewModelFactory()
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlacesViewModel::class.java)

        viewModel.getTextSearch("46.6379969,5.234819", "10000", "producteur", context!!.resources.getString(R.string.api_key_google)).observe(this, Observer<ArrayList<Place>> {
            if(it.isNotEmpty()){
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