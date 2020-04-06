package com.menard.ruralis.add_places

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.menard.ruralis.R
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity(), View.OnClickListener {

    /** ViewModel */
    private lateinit var viewModel: AddViewModel
    private lateinit var spinnerAdapter: TypeSpinnerAdapter
    /** Opening Hours Views */
    private lateinit var day: TextInputEditText
    private lateinit var hours: TextInputEditText
    private lateinit var openingsList: RecyclerView
    private val openingsAdapter = OpeningsListAdapter(this)
    private var listOfOpenings = ArrayList<String>()
    private var isEdit: Boolean = false
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddViewModel::class.java)
        bindViews()

        isEdit = intent.getBooleanExtra("Edit", false)
        id = intent.getStringExtra("Id")
        if (id != "") {
            viewModel.getPlaceDetailsById(id!!).observe(this, Observer {
                refreshViews(it)
            })
        }
    }

    private fun refreshViews(placeDetailed: PlaceDetailed) {
        add_name.setText(placeDetailed.name)
        //add_edit_type_spinner.setSelection(spinnerAdapter.getPosition(placeDetailed.type))
        add_address.setText(placeDetailed.address)
        contact_website.setText(placeDetailed.website)
        contact_phone_number.setText(placeDetailed.phone_number)
        if (placeDetailed.openingsHours != null) {
            openingsAdapter.setData(placeDetailed.openingsHours)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            add_save_btn -> {
                if (checkRequiredInfo()) {
                    val name: String = add_name.text.toString()
                    val type: String = add_edit_type_spinner.selectedItem.toString()
                    val address: String = add_address.text.toString()
                    val website: String = contact_website.text.toString()
                    val phoneNumber: String = contact_phone_number.text.toString()
                    viewModel.savePlace(id, type, name, address, listOfOpenings, website, phoneNumber, isEdit, "country:FR", resources.getString(R.string.api_key_google))
                    if (isEdit) {
                        val intent = Intent()
                        intent.putExtra("New place id", id)
                        setResult(5, intent)
                        finish()
                    }
                    finish()
                }else{
                    if(add_name.text.toString() == "") {
                        add_name.error = "Please write a name"
                    }
                    if(add_address.text.toString() == "") {
                        add_address.error = "Please write an address"
                    }
                }
            }
            add_opening_btn -> { addOpeningsToRecyclerView() }
        }
    }

    private fun checkRequiredInfo(): Boolean{
        return add_name.text.toString() != "" && add_address.text.toString() != ""
    }

    private fun addOpeningsToRecyclerView() {
        viewModel.addOpeningToRecyclerView(day.text.toString(), hours.text.toString())
            .observe(this, Observer {
                if (it == null) {
                    new_opening_day.error = "Please write a day"
                    new_opening_hours.error = "Please write an hour"
                } else {
                    listOfOpenings.add(it)
                    openingsAdapter.setData(listOfOpenings)
                    openingsAdapter.notifyDataSetChanged()
                    clearErrorAndEditText()
                }
            })
    }

    private fun clearErrorAndEditText() {
        day.text?.clear()
        hours.text?.clear()
        new_opening_day.isErrorEnabled = false
        new_opening_hours.isErrorEnabled = false
    }

    private fun bindViews() {
        day = findViewById(R.id.opening_day_edit)
        hours = findViewById(R.id.opening_hours_edit)
        add_opening_btn.setOnClickListener(this)
        add_save_btn.setOnClickListener(this)
        openingsList = findViewById(R.id.add_opening_recycler_view)
        openingsList.layoutManager = LinearLayoutManager(this)
        openingsList.adapter = openingsAdapter
        spinnerAdapter = TypeSpinnerAdapter(this, viewModel.getTypeEnumList())
        add_edit_type_spinner.adapter = spinnerAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
