package com.menard.ruralis.knowsit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.menard.ruralis.R
import com.menard.ruralis.details.DetailsActivity
import com.menard.ruralis.quiz.QuizHomeActivity
import com.menard.ruralis.search_places.MainActivity
import com.menard.ruralis.settings.SettingsActivity
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.header.view.*
import java.util.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener, FavoritesAdapter.OnItemClickListener {

    /** ViewModel */
    private lateinit var viewModel: HomeViewModel
    /** Favorites Adapter */
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        main_drawer.setNavigationItemSelectedListener(this)
        home_search_btn.setOnClickListener(this)
        home_refresh.setOnClickListener(this)
        home_list_fav.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        adapter = FavoritesAdapter(this, this)

        configureDrawerLayout()
        viewModel.homeLiveData.observe(this, Observer {
            if(it == null){
                knows_it_txt.text = "Seems like you're not connected to internet. Check your connexion and refresh"
                knows_it_img.visibility = View.GONE
            }else{
                knows_it_txt.text = it.info
                val enum = DrawableEnum.valueOf(it.drawable!!.toUpperCase(Locale.ROOT))
                val int = enum.drawableId
                knows_it_img.setImageResource(int)
                updateFavorites()
            }
        })
        configureHeader()
    }

    private fun updateFavorites() {
        viewModel.showAllFavorites().observe(this, Observer {
            if (it != null) {
                no_favorites.visibility = View.INVISIBLE
                home_list_fav.visibility = View.VISIBLE
                home_list_fav.adapter = adapter
                adapter.setData(it)
                adapter.notifyDataSetChanged()
            }else{
                no_favorites.visibility = View.VISIBLE
            }
        })
    }

    //-- CONFIGURATION --//

    private fun configureDrawerLayout() {
        val toogle = ActionBarDrawerToggle(
            this,
            activity_main_drawer_layout,
            R.string.main_drawer_open,
            R.string.main_drawer_close
        )
        toogle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.quantum_black_100)
        activity_main_drawer_layout.addDrawerListener(toogle)
        toogle.syncState()
    }

    /**
     * Configure the DrawerMenu, add a header
     */
    private fun configureHeader() {
        main_drawer.setNavigationItemSelectedListener(this)
        val view = main_drawer.getHeaderView(0)
        viewModel.updateHeader(FirebaseAuth.getInstance().currentUser?.displayName.toString(), FirebaseAuth.getInstance().currentUser?.photoUrl.toString(), FirebaseAuth.getInstance().currentUser?.email.toString()).observe(this, Observer {
            view.header_name.text = it.name
            Glide.with(applicationContext).load(Uri.parse((it.photo))).apply(RequestOptions.circleCropTransform()).centerCrop().into(view.header_photo)
        })
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
        if (activity_main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        }
    }


    override fun onClick(p0: View?) {
        when (p0) {
            home_search_btn -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            home_refresh -> {
                viewModel.getRandomKnowsIt()
            }
            home_quiz_btn -> {
                startActivity(Intent(this, QuizHomeActivity::class.java))
            }
        }
    }

    /**
     * Start DetailsActivity when click on one item of the recycler view
     */
    override fun onItemClicked(id: String, from: Boolean, photoUri: String?) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(Constants.INTENT_ID, id)
            putExtra(Constants.INTENT_FROM, from)
            putExtra(Constants.INTENT_URI, photoUri)
        }
        startActivity(intent)
    }

    //-- LIFECYCLE --//
    override fun onResume() {
        super.onResume()
        updateFavorites()
    }



}
