package com.menard.ruralis.settings

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.ColorUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.menard.ruralis.R
import com.menard.ruralis.utils.Constants
import com.rtugeek.android.colorseekbar.ColorSeekBar
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var buttonReset: MaterialButton
    private lateinit var saveButton: MaterialButton
    private lateinit var aroundUserEditTxt: TextInputEditText
    private lateinit var switchMapsBtn: SwitchCompat

    /** Shared Preferences */
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        bindViews()
    }

    private fun bindViews(){
        buttonReset = findViewById(R.id.settings_reset_color)
        buttonReset.setOnClickListener(this)
        saveButton = findViewById(R.id.settings_save_color)
        buttonReset.setOnClickListener(this)
        aroundUserEditTxt = findViewById(R.id.settings_edit_distance_user)
        val around = sharedPreferences.getString(Constants.PREF_SEARCH_AROUND, "50")
        aroundUserEditTxt.setText(around)
        switchMapsBtn = findViewById(R.id.settings_maps_switch_btn)
        val fromMaps = sharedPreferences.getBoolean(Constants.PREF_SEARCH_FROM_MAPS, true)
        switchMapsBtn.isChecked = fromMaps
    }

    override fun onClick(view: View?) {
        when(view){
            saveButton -> {
                val editor = sharedPreferences.edit()
                editor.putBoolean(Constants.PREF_SEARCH_FROM_MAPS, switchMapsBtn.isChecked)
                editor.putString(Constants.PREF_SEARCH_AROUND, aroundUserEditTxt.text.toString())
                editor.apply()
            }
            buttonReset -> {
                val editor = sharedPreferences.edit()
                editor.putBoolean(Constants.PREF_SEARCH_FROM_MAPS, true)
                editor.putString(Constants.PREF_SEARCH_AROUND, "50")
                editor.apply()
                onResume()
            }
        }
    }

}
