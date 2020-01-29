package com.menard.ruralis.controller.fragment

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
import com.menard.ruralis.adapter.ListAdapter
import com.menard.ruralis.model.textsearch.Result
import com.menard.ruralis.model.textsearch.TextSearch
import com.menard.ruralis.viewmodel.TextSearchViewModel
import com.menard.ruralis.viewmodel.injections.Injection

class ListViewFragment : Fragment() {


    /** FusedLocation */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    /** Places Client */
    private lateinit var placesClient: PlacesClient
    /** RecyclerView Adapter */
    private lateinit var adapter: ListAdapter
    private lateinit var listResult: List<Result>

    companion object {
        fun newInstance(): ListViewFragment {
            return ListViewFragment()
        }
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list_view, container, false)

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
        adapter = ListAdapter(requireContext())

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        //-- Places SDK initialisation --//
        Places.initialize(requireActivity(), context!!.resources.getString(R.string.api_key_google))
        placesClient = Places.createClient(requireActivity())

        getListOfPlaces()

        return view
    }

    private fun getListOfPlaces() {
        val viewModelFactory = Injection.provideTextSearchViewModelFactory()
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(TextSearchViewModel::class.java)

        viewModel.getTextSearch("46.6379969,5.234819", "10000", "maraîcher", context!!.resources.getString(R.string.api_key_google)).observe(this, Observer<TextSearch> {
            if(it != null){
               listResult = it.results!!
            }

            if(listResult.isNotEmpty()){
                recyclerView.adapter = adapter
                adapter.setData(listResult)
                adapter.notifyDataSetChanged()
            }
        })
    }
}