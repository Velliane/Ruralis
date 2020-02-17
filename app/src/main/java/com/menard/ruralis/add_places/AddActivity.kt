package com.menard.ruralis.add_places

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.google.android.material.button.MaterialButton
import com.menard.ruralis.R
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity(), View.OnClickListener {

    /** Save Button */
    private lateinit var saveBtn: MaterialButton
    /** Helper */
    private val placesHelper = PlacesHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        saveBtn = findViewById(R.id.add_save_btn)
        saveBtn.setOnClickListener(this)

        add_edit_type_spinner.adapter = ArrayAdapter<Types>(this, android.R.layout.simple_spinner_item ,Types.values())
    }

    override fun onClick(v: View?) {
        when(v){
            saveBtn -> {
                val name: String = add_name.text.toString()
                val type: String = add_edit_type_spinner.selectedItem.toString()
                val address: String = add_address.text.toString()
                placesHelper.createPlaces(type, name, address, "", "")
            }
        }
    }
}
