package com.menard.ruralis.knowsit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.menard.ruralis.R
import com.menard.ruralis.search_places.MainActivity
import com.menard.ruralis.settings.SettingsActivity
import com.menard.ruralis.utils.DrawableEnum

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    /** Drawer */
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView
    private var index: Int = 0

    private lateinit var searchBtn: MaterialButton
    private lateinit var refreshBtb: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bindViews()
        configureDrawerLayout()
        getRandomId()
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
            refreshBtb -> { getRandomId() }
        }
    }

    private fun getRandomId(){
        getAllIds()
            .addOnSuccessListener { documentSnapshot ->
            val list: ArrayList<String> = documentSnapshot.get("id") as ArrayList<String>
            list.shuffle()
            if(index == list.size){
                index = 0
            }

            getKnowsIt(list[index++])
                .addOnSuccessListener { documentSnapshot ->
                val knowsIt = documentSnapshot.toObject(KnowsIt::class.java)
                textView.text = knowsIt!!.info
                val enum = DrawableEnum.valueOf(knowsIt.drawable!!.toUpperCase())
                val int = enum.drawableId
                imageView.setImageResource(int)
            }
        }

    }
}
