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
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.search_places.MainViewModel
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection

class DetailsActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener{

    /** Viewpager */
    private lateinit var viewPager: ViewPager
    /** Place id */
    private lateinit var placeId: String
    /** From Boolean */
    private var fromRuralis: Boolean = false
    /** Place ViewModel */
    private lateinit var viewModel: DetailsViewModel
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
        val viewModelFactory = Injection.provideDetailsViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    private fun getPlaceFromViewModel(){
        viewModel.getPlaceAccordingItsOrigin(fromRuralis, placeId, getString(R.string.details_field), getString(R.string.api_key_google))
        viewModel.placeLiveData.observe(this, Observer {
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, this, it)
            name.text = it.name
        })
    }

    private fun configureViewPager() {
        viewPager = findViewById(R.id.details_fragments_container)
        //-- Tabs --//
        val tabLayout = findViewById<TabLayout>(R.id.details_tab_layout)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setOnTabSelectedListener(this)
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
