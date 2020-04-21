package com.menard.ruralis.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.menard.ruralis.R
import com.menard.ruralis.utils.Constants
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    /** Shared Preferences */
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        bindViews()
    }

    private fun bindViews(){
        settings_reset.setOnClickListener(this)
        settings_save.setOnClickListener(this)
        val around = sharedPreferences.getString(Constants.PREF_SEARCH_AROUND, "50")
        settings_edit_distance_user.setText(around)
        val fromMaps = sharedPreferences.getBoolean(Constants.PREF_SEARCH_FROM_MAPS, true)
        settings_maps_switch_btn.isChecked = fromMaps
    }

    override fun onClick(view: View?) {
        when(view){
            settings_save -> {
                val editor = sharedPreferences.edit()
                editor.putBoolean(Constants.PREF_SEARCH_FROM_MAPS, settings_maps_switch_btn.isChecked)
                editor.putString(Constants.PREF_SEARCH_AROUND, settings_edit_distance_user.text.toString())
                editor.apply()
                Snackbar.make(settings_container, getString(R.string.settings_save_snackbar), Snackbar.LENGTH_SHORT).show()
            }
            settings_reset -> {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle(getString(R.string.settings_initialize_title))
                alertDialog.setMessage(getString(R.string.settings_initialize))
                alertDialog.setPositiveButton(getString(R.string.yes)){ _, _ ->
                    val editor = sharedPreferences.edit()
                    editor.putBoolean(Constants.PREF_SEARCH_FROM_MAPS, true)
                    editor.putString(Constants.PREF_SEARCH_AROUND, "50")
                    editor.apply()
                    finish()
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }
                alertDialog.setNegativeButton(getString(R.string.no)){ dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.create().show()
            }
        }
    }

}
