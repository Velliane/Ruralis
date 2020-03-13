package com.menard.ruralis.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.menard.ruralis.R
import com.menard.ruralis.add_places.AddActivity
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.search_places.MainViewModel
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import com.menard.ruralis.utils.SharedPreference

class DetailsActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener, View.OnClickListener {

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
    /** FAB for Favorites */
    private lateinit var favoriteFab: FloatingActionButton
    /** SharedPreference class for managing Favorites */
    private val sharedPreference = SharedPreference()
    private var favorite = Favorite()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        name = findViewById(R.id.details_name)
        favoriteFab = findViewById(R.id.details_favorite_fab)
        favoriteFab.setOnClickListener(this)

        placeId = intent.getStringExtra(Constants.INTENT_ID)!!
        fromRuralis = intent.getBooleanExtra(Constants.INTENT_FROM, false)
        favorite = Favorite(placeId, fromRuralis)

        configureViewModel()
        getPlaceFromViewModel()
        setFavorites()
        configureViewPager()
    }

    private fun setFavorites() {
        val check = viewModel.checkFavorites(this, favorite)
        if(check){
            favoriteFab.setImageResource(R.drawable.star_clicked)
            favoriteFab.tag = "Favorite"
        }else{
            favoriteFab.setImageResource(R.drawable.star_unclicked)
            favoriteFab.tag = "No favorite"
        }
    }


    private fun configureViewModel() {
        val viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    private fun getPlaceFromViewModel(){
        viewModel.getPlaceAccordingItsOrigin(fromRuralis, placeId, getString(R.string.details_field), getString(R.string.api_key_google)).observe(this, Observer {
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

    override fun onClick(view: View?) {
        when(view){
            favoriteFab -> {
                if(favoriteFab.tag == "No favorite"){
                    sharedPreference.addFavorite(this, favorite)
                    favoriteFab.setImageResource(R.drawable.star_clicked)
                    favoriteFab.tag = "Favorite"
                }else{
                    sharedPreference.removeFavorite(this, favorite)
                    favoriteFab.setImageResource(R.drawable.star_unclicked)
                    favoriteFab.tag = "No favorite"
                }
            }
        }
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
    }


}
