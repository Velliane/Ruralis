package com.menard.ruralis.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.menard.ruralis.R
import com.menard.ruralis.add_places.AddActivity

class DetailsActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener{

    /** Viewpager */
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        configureViewPager()
    }

    private fun configureViewPager() {
        viewPager = findViewById(R.id.details_fragments_container)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, this)
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
