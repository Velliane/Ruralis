package com.menard.ruralis.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.menard.ruralis.R
import com.menard.ruralis.add_places.AddActivity
import com.menard.ruralis.add_places.Place
import com.menard.ruralis.search_places.view_model.PlacesViewModel
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import kotlin.properties.Delegates

class DetailsActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener{

    /** Viewpager */
    private lateinit var viewPager: ViewPager
    /** Place id */
    private lateinit var placeId: String
    /** From Boolean */
    private var fromRuralis: Boolean = false
    /** Place ViewModel */
    private lateinit var viewModel: PlacesViewModel
    /** Name */
    private lateinit var name: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        name = findViewById(R.id.details_name)

        placeId = intent.getStringExtra(Constants.INTENT_ID)!!
        fromRuralis = intent.getBooleanExtra(Constants.INTENT_FROM, false)

        configureViewModel()
        getPlaceFromViewModel()
        configureViewPager()
    }



    private fun configureViewModel() {
        val viewModelFactory = Injection.providePlacesViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlacesViewModel::class.java)
    }

    private fun getPlaceFromViewModel(){
        if(fromRuralis){
            viewModel.getPlaceFromFirestoreById(placeId)
            viewModel.place.observe(this, Observer {
                setViewPageAdapter(it)
                name.text = it.name
            })
        }else{
            viewModel.getDetailsById(placeId, "name,rating,formatted_phone_number,place_id,photo,type,opening_hours,vicinity,geometry,website,review", getString(R.string.api_key_google)).observe(this, Observer {
                setViewPageAdapter(it)
                name.text = it.name
            })
        }
    }

    private fun configureViewPager() {
        viewPager = findViewById(R.id.details_fragments_container)
        //-- Tabs --//
        val tabLayout = findViewById<TabLayout>(R.id.details_tab_layout)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setOnTabSelectedListener(this)

    }

    private fun setViewPageAdapter(place: Place) {
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, this, place)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_menu_modify -> {
                startActivity(Intent(this, AddActivity::class.java))
                return true
            }
        }
        return true
    }


    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
    }
}
