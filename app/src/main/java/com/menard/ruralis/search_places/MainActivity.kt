package com.menard.ruralis.search_places

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.menard.ruralis.R
import com.menard.ruralis.add_places.AddActivity
import com.menard.ruralis.search_places.list.ListViewFragment
import com.menard.ruralis.search_places.map.MapViewFragment
import com.menard.ruralis.settings.SettingsActivity
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener{

    /** Toolbar*/
    private lateinit var toolbar: Toolbar
    /** Drawer */
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    /**Bottom Navigation View */
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewModel: MainViewModel
    /** Header Views */
    private lateinit var photo: CircleImageView
    private lateinit var name: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        configureDrawerLayout()
        // Set BottomNavigationView to ListFragment by default
        bottomNavigationView.selectedItemId = R.id.action_list_view
        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        configureHeader()
    }

    //-- CONFIGURATION --//
    /**
     * Bind views of activity
     */
    private fun bindViews() {
        toolbar = findViewById(R.id.activity_main_toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)
        navigationView = findViewById(R.id.main_drawer)
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

    /**
     * Configure the DrawerMenu, add a header
     */
    private fun configureHeader() {
        navigationView.setNavigationItemSelectedListener(this)
        val view = navigationView.getHeaderView(0)
        photo = view.findViewById(R.id.header_photo)
        name = view.findViewById(R.id.header_name)
        viewModel.updateHeader(FirebaseAuth.getInstance().currentUser?.displayName.toString(),FirebaseAuth.getInstance().currentUser?.photoUrl.toString(), FirebaseAuth.getInstance().currentUser?.email.toString()).observe(this, Observer {
            name.text = it.name
            Glide.with(applicationContext).load(Uri.parse((it.photo))).apply(RequestOptions.circleCropTransform()).centerCrop().into(photo)
        })
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.toolbar_menu_filter)?.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_menu_add -> {
                val intent = Intent(this, AddActivity::class.java)
                intent.putExtra("Edit", false)
                intent.putExtra("Id", "")
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //-- LISTENER FOR NAVIGATION VIEW AND BOTTOM NAVIGATION VIEW --//
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
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
            R.id.action_log_out -> {
                viewModel.logOut()
                return true
            }
        }
        return false
    }

}
