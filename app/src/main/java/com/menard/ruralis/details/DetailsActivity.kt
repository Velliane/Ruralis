package com.menard.ruralis.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.menard.ruralis.R
import com.menard.ruralis.add_places.AddActivity
import com.menard.ruralis.details.comments.CommentsFragment
import com.menard.ruralis.details.contacts.ContactFragment
import com.menard.ruralis.details.fragment.InfosFragment
import com.menard.ruralis.details.photos.PhotoFragment
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener, View.OnClickListener {

    /** Viewpager */
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var toolbar: Toolbar

    /** Fragments */
    private lateinit var fragmentInfo: InfosFragment
    private lateinit var contactFragment: ContactFragment
    private lateinit var photoFragment: PhotoFragment
    private lateinit var commentsFragment: CommentsFragment
    /** Place id */
    private var placeId: String? = null
    /** From Boolean */
    private var fromRuralis: Boolean = false
    private var photoUri: String? = null
    /** Place ViewModel */
    private lateinit var viewModel: DetailsViewModel
    /** Name */
    private lateinit var name: TextView
    /** FAB for Favorites */
    private lateinit var favoriteFab: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        name = findViewById(R.id.details_name)
        favoriteFab = findViewById(R.id.details_favorite_fab)
        favoriteFab.setOnClickListener(this)
        toolbar = findViewById(R.id.details_toolbar)
        setSupportActionBar(toolbar)

        placeId = intent.getStringExtra(Constants.INTENT_ID)!!
        fromRuralis = intent.getBooleanExtra(Constants.INTENT_FROM, false)
        photoUri = intent.getStringExtra(Constants.INTENT_URI)
        configureViewModel()
        getPlaceFromViewModel()
        configureViewPager()
        setFavorites()
    }

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


    private fun configureViewModel() {
        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)
    }

    private fun getPlaceFromViewModel() {
        viewModel.getPlaceAccordingItsOrigin(fromRuralis, placeId!!, getString(R.string.details_field), getString(R.string.api_key_google)
        ).observe(this, Observer {
            pagerAdapter = ViewPagerAdapter(supportFragmentManager, this, it)
            viewPager.adapter = pagerAdapter
            name.text = it.name
            pagerAdapter.startUpdate(viewPager)
            fragmentInfo = pagerAdapter.instantiateItem(viewPager, 0) as InfosFragment
            photoFragment = pagerAdapter.instantiateItem(viewPager, 1) as PhotoFragment
            contactFragment = pagerAdapter.instantiateItem(viewPager, 2) as ContactFragment
            commentsFragment = pagerAdapter.instantiateItem(viewPager, 3) as CommentsFragment
            pagerAdapter.finishUpdate(viewPager)
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
        when (item.itemId) {
            R.id.toolbar_menu_modify -> {
                if (fromRuralis) {
                    val intent = Intent(this, AddActivity::class.java)
                    intent.putExtra("Edit", true)
                    intent.putExtra("Id", placeId)
                    startActivityForResult(intent, 5)
                    return true
                } else {
                    Snackbar.make(
                        details_container,
                        "Cet établissement ne peut pas être modifié",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return true
    }

    override fun onClick(view: View?) {
        when (view) {
            favoriteFab -> {
                if (favoriteFab.tag == "No favorite") {
                    viewModel.addToFavorites(placeId, fromRuralis, photoUri, name.text.toString())
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

    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        placeId = data?.getStringExtra("New place id")
        if (placeId != null) {
            viewModel.getPlaceFromFirestoreById(placeId)
            viewModel.placeLiveData.observe(this, Observer {
                pagerAdapter.refreshData(it)
                name.text = it.name
            })
        }
    }

}
