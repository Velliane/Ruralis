package com.menard.ruralis.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.menard.ruralis.R
import com.menard.ruralis.add_places.AddActivity
import com.menard.ruralis.details.comments.CommentsFragment
import com.menard.ruralis.details.contacts.ContactFragment
import com.menard.ruralis.details.info.InfosFragment
import com.menard.ruralis.details.photos.PhotoFragment
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener, View.OnClickListener {

    /** Viewpager */
    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var toolbar: Toolbar
    /** Place id */
    private var placeId: String? = null
    /** From Boolean */
    private var fromRuralis: Boolean = false
    private var photoUri: String? = null
    /** Place ViewModel */
    private lateinit var viewModel: DetailsViewModel
    /** FAB for Favorites */
    private lateinit var favoriteFab: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        favoriteFab = findViewById(R.id.details_favorite_fab)
        favoriteFab.setOnClickListener(this)
        toolbar = findViewById(R.id.details_toolbar)
        viewPager = findViewById(R.id.details_fragments_container)
        setSupportActionBar(toolbar)

        placeId = intent.getStringExtra(Constants.INTENT_ID)!!
        fromRuralis = intent.getBooleanExtra(Constants.INTENT_FROM, false)
        photoUri = intent.getStringExtra(Constants.INTENT_URI)
        configureViewModel()
        getPlaceFromViewModel()
        setFavorites()
    }

    private fun getPlaceFromViewModel() {
        viewModel.getPlaceAccordingItsOrigin(fromRuralis, placeId!!, getString(R.string.details_field), getString(R.string.api_key_google)
        ).observe(this, Observer {
            pagerAdapter = ViewPagerAdapter(this, it, this)
            viewPager.adapter = pagerAdapter
            configureViewPager()
            details_name.text = it.name
        })
    }

    /**
     * Check if place is save in FavoritesDatabase
     */
    private fun setFavorites() {
        viewModel.checkIfAlreadyInFavorites(placeId!!).observe(this, Observer {
            if (it) {
                favoriteFab.setImageResource(R.drawable.star_clicked)
                favoriteFab.tag = "Favorite"
            } else {
                favoriteFab.setImageResource(R.drawable.star_unclicked)
                favoriteFab.tag = "No favorite"
            }
        })
    }

    //-- CONFIGURATION --//
    private fun configureViewModel() {
        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    private fun configureViewPager() {
        //-- Tabs --//
        val tabLayout = findViewById<TabLayout>(R.id.details_tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(DetailsCategoryEnum.values()[position].title)
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_menu_modify -> {
                if (fromRuralis) {
                    val intent = Intent(this, AddActivity::class.java)
                    intent.putExtra(Constants.INTENT_EDIT, true)
                    intent.putExtra(Constants.INTENT_ID, placeId)
                    startActivityForResult(intent, 5)
                    return true
                } else {
                    Snackbar.make(
                        details_container,
                        getString(R.string.cannot_modify),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return true
    }

    /** When click on Favorite FAB */
    override fun onClick(view: View?) {
        when (view) {
            favoriteFab -> {
                if (favoriteFab.tag == "No favorite") {
                    viewModel.addToFavorites(placeId, fromRuralis, photoUri, details_name.text.toString())
                    favoriteFab.setImageResource(R.drawable.star_clicked)
                    favoriteFab.tag = "Favorite"
                } else {
                    viewModel.deleteFromFavorites(placeId!!)
                    favoriteFab.setImageResource(R.drawable.star_unclicked)
                    favoriteFab.tag = "No favorite"
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        placeId = data?.getStringExtra("New place id")
        if (placeId != null) {
            viewModel.getPlaceFromFirestoreById(placeId)
            viewModel.placeLiveData.observe(this, Observer {
                pagerAdapter.refreshData(it)
                details_name.text = it.name
            })
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        TODO("Not yet implemented")
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        TODO("Not yet implemented")
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        TODO("Not yet implemented")
    }

}
