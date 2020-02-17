package com.menard.ruralis.settings

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.menard.ruralis.R
import com.rtugeek.android.colorseekbar.ColorSeekBar

class SettingsActivity : AppCompatActivity() {

    /** ColorSeekBar for changing theme's color */
    private lateinit var colorChangePrimary: ColorSeekBar
    private lateinit var colorChangeAccent: ColorSeekBar
    private lateinit var titleChangePrimary: TextView
    private lateinit var titlePrimaryDark: TextView
    private lateinit var titleChangeAccent: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        bindViews()
    }

    private fun bindViews(){
        titleChangePrimary = findViewById(R.id.settings_color_title)
        titlePrimaryDark = findViewById(R.id.settings_color_dark)
        titleChangeAccent = findViewById(R.id.settings_color_accent)

        colorChangePrimary = findViewById(R.id.settings_slider_color_primary)
        colorChangePrimary.setOnColorChangeListener { position, alpha, color ->
            titleChangePrimary.setTextColor(color)
            val darkerColor = ColorUtils.blendARGB(color, Color.BLACK, 0.4f)
            titlePrimaryDark.setTextColor(darkerColor)
        }
        colorChangeAccent = findViewById(R.id.settings_slider_color_accent)
        colorChangeAccent.setOnColorChangeListener { position, alpha, color ->
            titleChangeAccent.setTextColor(color)
        }
    }

}
