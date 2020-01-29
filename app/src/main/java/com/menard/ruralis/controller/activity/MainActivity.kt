package com.menard.ruralis.controller.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.menard.ruralis.R
import com.menard.ruralis.controller.fragment.HomeFragment
import com.menard.ruralis.controller.fragment.ListViewFragment
import com.menard.ruralis.controller.fragment.MapViewFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    /** Toolbar*/
    private lateinit var toolbar: Toolbar
    /** Drawer */
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    /**Bottom Navigation View */
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        configureDrawerLayout()

        // Set BottomNavigationView to HomeFragment by default
        bottomNavigationView.selectedItemId = R.id.action_home
    }

    //-- CONFIGURATION --//
    /**
     * Bind views of activity
     */
    private fun bindViews() {
        toolbar = findViewById(R.id.activity_main_toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)
        navigationView = findViewById(R.id.activity_main_navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    private fun configureDrawerLayout() {
        val toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.main_drawer_open,
            R.string.main_drawer_close
        )
        toogle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.quantum_black_100)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
    }

    private fun configureNavigationView() {
        //TODO Header
    }

    //-- FRAGMENT --//
    private fun addFragmentToLayout(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    //-- TOOLBAR MENU --//
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    //-- LISTENER FOR NAVIGATION VIEW AND BOTTOM NAVIGATION VIEW --//
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_home -> {
                val homeFragment = HomeFragment.newInstance()
                addFragmentToLayout(homeFragment)
                return true
            }
            R.id.action_list_view -> {
                val listViewFragment = ListViewFragment.newInstance()
                addFragmentToLayout(listViewFragment)
                return true
            }
            R.id.action_map_view -> {
                val mapViewFragment = MapViewFragment.newInstance()
                addFragmentToLayout(mapViewFragment)
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}
