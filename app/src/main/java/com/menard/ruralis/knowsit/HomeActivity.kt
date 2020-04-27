package com.menard.ruralis.knowsit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import java.util.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener, FavoritesAdapter.OnItemClickListener {

    /** ViewModel */
    private lateinit var viewModel: HomeViewModel
    /** Favorites Adapter */
    private lateinit var adapter: FavoritesAdapter
    /** Shared Preferences */
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        main_drawer.setNavigationItemSelectedListener(this)
        home_search_btn.setOnClickListener(this)
        home_quiz_btn.setOnClickListener(this)
        home_list_fav.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        adapter = FavoritesAdapter(this, this)
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val guide = sharedPreferences.getBoolean(Constants.PREF_GUIDE_HOME, false)
        if(!guide){
            showGuide(getString(R.string.home_guide_title_1),
                getString(R.string.home_guide_text_1),
                knows_it_txt, 1)
        }
        configureDrawerLayout()
        viewModel.homeLiveData.observe(this, Observer {
            if(it == null){
                knows_it_txt.text = getString(R.string.not_connected_error)
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


    private fun showGuide(title: String, text: String, view: View, type: Int) {
        GuideView.Builder(this)
            .setTitle(title)
            .setContentText(text)
            .setTargetView(view)
            .setContentTextSize(15)
            .setTitleTextSize(17)
            .setDismissType(DismissType.anywhere)
            .setGuideListener {
                when(type){
                    1 -> showGuide(getString(R.string.home_guide_title_2),
                        getString(R.string.home_guide_text_2),
                    home_search_btn, 2)
                    2 -> showGuide(getString(R.string.home_quide_title_3), getString(R.string.home_guide_text_3),
                        home_quiz_btn, 3)
                    3 -> showGuide(getString(R.string.home_guide_title_4), getString(R.string.home_guide_text_4),
                        home_list_fav, 4)
                    4 -> sharedPreferences.edit().putBoolean(Constants.PREF_GUIDE_HOME, true).apply()
                }
            }
            .build()
            .show()
    }
}
