package com.menard.ruralis.controller.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.menard.ruralis.R
import com.menard.ruralis.model.KnowsIt

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    /** Drawer */
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView
    private var index: Int = 0

    private lateinit var searchBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bindViews()
        configureDrawerLayout()

        val knowsIt = getKnowsIt()
        textView.text = knowsIt.info
        imageView.setImageResource(knowsIt.drawable_id!!)
    }

    //-- CONFIGURATION --//
    /**
     * Bind views of activity
     */
    private fun bindViews() {
        drawerLayout = findViewById(R.id.activity_main_drawer_layout)
        navigationView = findViewById(R.id.activity_main_navigation_view)
        navigationView.setNavigationItemSelectedListener(this)
        textView = findViewById(R.id.knows_it_txt)
        imageView = findViewById(R.id.knows_it_img)
        searchBtn = findViewById(R.id.home_search_btn)
        searchBtn.setOnClickListener(this)
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

    private fun configureNavigationView() {
        //TODO Header
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    private fun getKnowsIt(): KnowsIt {

        val list = ArrayList<KnowsIt>()
        val knowsIt1 = KnowsIt(0, "Les poules ne pondent pas un oeuf par jour, mais un toutes les 26 heures. De plus, plus la durée du jour décroit, moins elles pondent, jusqu'à s'arrêter complètement en Hiver. C'est pour cela que les éleveurs utilisent un système d'éclairage, pour nous permettre de manger des oeufs toute l'année.", R.drawable.egg)
        val knowsIt2 = KnowsIt(1, "Une cinquantaine de races de bovin sont présentes sur le territoire français", R.drawable.cow)
        val knowsIt3 = KnowsIt(2, "Il peut arriver que vous surpreniez votre lapin en train de manger ses crottes. Rassurez-vous, ça n'a rien de bizarre. Le lapin est un coprophage. Afin de palier au manque d'efficacité de son système digestif, il doit ingérer deux fois ses aliments pour les digérer entièrement.", R.drawable.rabbit)
        list.add(knowsIt1)
        list.add(knowsIt2)
        list.add(knowsIt3)
        list.shuffle()

        if(index == list.size){
            index = 0
        }
        return list[index++]

    }

    override fun onClick(p0: View?) {
        when(p0){
            searchBtn -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

    }
}
