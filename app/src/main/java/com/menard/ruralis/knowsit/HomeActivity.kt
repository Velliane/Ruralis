package com.menard.ruralis.knowsit

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.menard.ruralis.R
import com.menard.ruralis.search_places.MainActivity
import com.menard.ruralis.search_places.PlaceForList
import com.menard.ruralis.settings.SettingsActivity
import com.menard.ruralis.utils.Injection
import com.menard.ruralis.utils.SharedPreference
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    /** Drawer */
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    private lateinit var searchBtn: MaterialButton
    private lateinit var refreshBtb: ImageView

    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var textNoFav: TextView
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        val sharedPreference = SharedPreference()
        adapter = FavoritesAdapter(this)

        bindViews()
        configureDrawerLayout()
        getKnowsIt()
        updateFavorites(sharedPreference)
    }

    private fun updateFavorites(sharedPreference: SharedPreference) {
        var placeFavorites = ArrayList<PlaceForList>()
        val favorites = sharedPreference.getFavorites(this)
        if(favorites != null) {
            textNoFav.visibility = View.INVISIBLE
            favoritesRecyclerView.visibility = View.VISIBLE
            for (place in favorites) {
                viewModel.getPlaceFavoriteAccordingItsOrigin(place!!.fromRuralis, place.id, getString(R.string.details_field), getString(R.string.api_key_google)).observe(this, Observer {
                    placeFavorites.add(it)
                })
            }
            favoritesRecyclerView.adapter = adapter
            adapter.setData(placeFavorites)
            adapter.notifyDataSetChanged()
        }
    }

    private fun getKnowsIt() {
        viewModel.getRandomKnowsIt()
        viewModel.randomKnowsIt.observe(this, Observer {
            textView.text = it.info
            val enum = DrawableEnum.valueOf(it.drawable!!.toUpperCase(Locale.ROOT))
            val int = enum.drawableId
            imageView.setImageResource(int)
        })
    }

    //-- CONFIGURATION --//
    /**
     * Bind views of activity
     */
    private fun bindViews() {
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)
        navigationView = findViewById(R.id.main_drawer)
        navigationView.setNavigationItemSelectedListener(this)
        textView = findViewById(R.id.knows_it_txt)
        imageView = findViewById(R.id.knows_it_img)
        searchBtn = findViewById(R.id.home_search_btn)
        searchBtn.setOnClickListener(this)
        refreshBtb = findViewById(R.id.home_refresh)
        refreshBtb.setOnClickListener(this)
        favoritesRecyclerView = findViewById(R.id.home_list_fav)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        textNoFav = findViewById(R.id.no_favorites)
    }

    private fun configureDrawerLayout() {
        val toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.main_drawer_open,
            R.string.main_drawer_close
        )
        toogle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.quantum_black_100)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return false
    }

    //-- TOOLBAR MENU --//
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }


    override fun onClick(p0: View?) {
        when(p0){
            searchBtn -> { startActivity(Intent(this, MainActivity::class.java)) }
            refreshBtb -> { getKnowsIt() }
        }
    }

}
