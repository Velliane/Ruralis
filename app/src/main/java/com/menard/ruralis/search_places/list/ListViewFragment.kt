package com.menard.ruralis.search_places.list

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.menard.ruralis.BaseFragment
import com.menard.ruralis.R
import com.menard.ruralis.details.DetailsActivity
import com.menard.ruralis.search_places.textsearch_model.Result
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.custom_search.view.*
import kotlinx.android.synthetic.main.fragment_list_view.*
import kotlinx.android.synthetic.main.fragment_list_view.view.*

class ListViewFragment : BaseFragment(),
    ListAdapter.OnItemClickListener, View.OnClickListener {

    /** Places Client */
    private lateinit var placesClient: PlacesClient

    /** RecyclerView Adapter */
    private lateinit var adapter: ListAdapter
    private lateinit var listResult: List<Result>

    /** Shared Preferences */
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: ListViewModel

    /** Radius */
    private var radius: String? = ""

    /** Add fromMaps */
    private var fromMaps: Boolean = true
    private lateinit var textSearch: AppCompatAutoCompleteTextView
    private lateinit var searchBtn: ImageButton

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
        radius = sharedPreferences.getString(Constants.PREF_SEARCH_AROUND, "50")
        //-- Places SDK initialisation --//
        Places.initialize(requireActivity(), context!!.resources.getString(R.string.api_key_google))
        placesClient = Places.createClient(requireActivity())
        view.list_progress.visibility = View.VISIBLE
        //-- Show Places --//
        if (checkPermissions()) {
            getUserLocation()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelFactory = Injection.provideViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ListViewModel::class.java)
    }

    private fun bindViews(view: View) {
        listResult = ArrayList()
        view.fragment_list_recycler_view.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.HORIZONTAL))
        view.fragment_list_recycler_view.layoutManager = LinearLayoutManager(activity)
        adapter = ListAdapter(
            this,
            requireContext()
        )
        view.fragment_list_recycler_view.adapter = adapter
        textSearch = view.findViewById(R.id.edit_search)
        view.edit_search.addTextChangedListener {
            viewModel.filter(it.toString())
            viewModel.filteredPlacesLiveData.observe(this, Observer { list ->
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
        when (item.itemId) {
            R.id.toolbar_menu_filter -> {
                if (activity_main_search.visibility == View.GONE) {
                    activity_main_search.visibility = View.VISIBLE
                } else {
                    activity_main_search.visibility = View.GONE
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUserLocation() {
        var location: Location?
        viewModel.locationLiveData.observe(this, Observer {
            if(it != null){
                location = it
                viewModel.initViewModel(radius + "000", Constants.QUERY_GM, fromMaps, location)
                showAllPlaces()
            }
        })
    }

    /**
     * Get places from Firestore and GoogleMaps
     */
    private fun showAllPlaces() {
        viewModel.allPlaceLiveData.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                adapter.setData(it)
            }
        })
        viewModel.progressLiveData.observe(this, Observer {
            if (it) {
                view?.list_progress?.visibility = View.INVISIBLE
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
        when (view) {
            searchBtn -> {
                viewModel.filter(textSearch.text.toString())
                viewModel.filteredPlacesLiveData.observe(this, Observer {
                    adapter.setData(it)
                })
            }
        }
    }

}