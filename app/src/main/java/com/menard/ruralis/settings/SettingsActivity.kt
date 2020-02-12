package com.menard.ruralis.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.menard.ruralis.R
import com.rtugeek.android.colorseekbar.ColorSeekBar

class SettingsActivity : AppCompatActivity(), ColorSeekBar.OnColorChangeListener {

    private lateinit var colorChangePrimary: ColorSeekBar
    private lateinit var titleChangePrimary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        bindViews()
    }

    private fun bindViews(){
        titleChangePrimary = findViewById(R.id.settings_color_title)
        colorChangePrimary = findViewById(R.id.settings_slider_color_primary)
        colorChangePrimary.setOnColorChangeListener(this)
    }


    override fun onColorChangeListener(colorBarPosition: Int, alphaBarPosition: Int, color: Int) {
        titleChangePrimary.setTextColor(color)
    }
}
