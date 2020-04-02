package com.menard.ruralis.settings

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.google.android.material.button.MaterialButton
import com.menard.ruralis.R
import com.menard.ruralis.utils.Constants
import com.rtugeek.android.colorseekbar.ColorSeekBar
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    /** ColorSeekBar for changing theme's color */
    private lateinit var colorChangePrimary: ColorSeekBar
    private lateinit var colorChangeAccent: ColorSeekBar
    private lateinit var titleChangePrimary: TextView
    private lateinit var titlePrimaryDark: TextView
    private lateinit var titleChangeAccent: TextView

    private lateinit var buttonReset: MaterialButton
    private lateinit var saveButton: MaterialButton

    /** Shared Preferences */
    private lateinit var sharedPreferences: SharedPreferences




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        bindViews()
    }

    private fun bindViews(){
        titleChangePrimary = findViewById(R.id.settings_color_title)
        titlePrimaryDark = findViewById(R.id.settings_color_dark)
        titleChangeAccent = findViewById(R.id.settings_color_accent)
        buttonReset = findViewById(R.id.settings_reset_color)
        buttonReset.setOnClickListener(this)
        saveButton = findViewById(R.id.settings_save_color)
        buttonReset.setOnClickListener(this)
        val colorPrimary = sharedPreferences.getInt(Constants.PREF_PRIMARY_COLOR, 0)
        if(colorPrimary != 0){
            buttonReset.setTextColor(colorPrimary)
        }

        colorChangePrimary = findViewById(R.id.settings_slider_color_primary)
        colorChangePrimary.setOnColorChangeListener { position, alpha, color ->
            titleChangePrimary.setTextColor(color)
            colorChangePrimary.getColorIndexPosition(color)
            Log.d("Color primary", color.toString())
            val darkerColor = ColorUtils.blendARGB(color, Color.BLACK, 0.4f)
            Log.d("darker", darkerColor.toString())
            titlePrimaryDark.setTextColor(darkerColor)
        }
        colorChangeAccent = findViewById(R.id.settings_slider_color_accent)
        colorChangeAccent.setOnColorChangeListener { position, alpha, color ->
            titleChangeAccent.setTextColor(color)
            Log.d("Color accent", color.toString())
        }
    }

    override fun onClick(view: View?) {
        when(view){
            buttonReset -> {
                colorChangePrimary.color = Constants.COLOR_PRIMARY_DEFAULT
                colorChangeAccent.color = Constants.COLOR_ACCENT_DEFAULT
            }
            buttonReset -> {
                val editor = sharedPreferences.edit()
                editor.putInt(Constants.PREF_PRIMARY_COLOR, colorChangePrimary.color)
                editor.putInt(Constants.PREF_ACCENT_COLOR, colorChangeAccent.color)
                editor.apply()
            }
        }
    }

}
