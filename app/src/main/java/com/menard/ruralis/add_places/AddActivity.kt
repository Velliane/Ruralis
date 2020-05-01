package com.menard.ruralis.add_places

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hootsuite.nachos.chip.ChipInfo
import com.hootsuite.nachos.terminator.ChipTerminatorHandler
import com.menard.ruralis.R
import com.menard.ruralis.utils.Constants
import com.menard.ruralis.utils.Injection
import com.menard.ruralis.utils.getListOfDay
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity(), View.OnClickListener, OpeningsListAdapter.OnItemClickListener {

    /** ViewModel */
    private lateinit var viewModel: AddViewModel
    private lateinit var spinnerAdapter: TypeSpinnerAdapter
    /** Opening Hours Views */
    private lateinit var openingsList: RecyclerView
    private val openingsAdapter = OpeningsListAdapter(this, this, true)
    private var listOfOpenings = ArrayList<String>()

    private var isEdit: Boolean = false
    private var id: String? = null
    /** Container */
    private lateinit var container: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddViewModel::class.java)
        bindViews()
        isEdit = intent.getBooleanExtra(Constants.INTENT_EDIT, false)
        id = intent.getStringExtra(Constants.INTENT_ID)
        if (id != "") {
            viewModel.getPlaceDetailsById(id!!).observe(this, Observer {
                refreshViews(it)
            })
        }
    }

    private fun refreshViews(placeDetailed: PlaceDetailed) {
        add_name.setText(placeDetailed.name)
        add_edit_type_spinner.setSelection(spinnerAdapter.getPosition(TypesEnum.valueOf(placeDetailed.type)))
        add_address.setText(placeDetailed.address)
        contact_website.setText(placeDetailed.website)
        contact_phone_number.setText(placeDetailed.phone_number)
        if (placeDetailed.openingsHours != null) {
            val list = placeDetailed.openingsHours.split(",").toTypedArray()
            listOfOpenings.addAll(list)
            openingsAdapter.setData(list.asList())
        }
        if(placeDetailed.tags != null || placeDetailed.tags != ""){
            val tags = placeDetailed.tags?.split(",")?.toTypedArray()
            val listOfChip = ArrayList<ChipInfo>()
            for(item in tags!!){
                val chip = ChipInfo(item, null)
                listOfChip.add(chip)
            }
            edit_tags.setTextWithChips(listOfChip)
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
                    viewModel.savePlace(id, viewModel.formatListOfTags(edit_tags.chipValues), type, name, address, listOfOpenings, website, phoneNumber, isEdit, "country:FR", resources.getString(R.string.api_key_google))
                    if (isEdit) {
                        val intent = Intent()
                        intent.putExtra(Constants.INTENT_EDIT, id)
                        setResult(5, intent)
                        finish()
                    } else {
                        add_name.text = null
                        add_address.text = null
                        contact_website.text = null
                        contact_phone_number.text = null
                        viewModel.addingSuccessLiveData.observe(this, Observer {
                            if (it) {
                                listOfOpenings.clear()
                                openingsAdapter.setData(listOfOpenings)
                                showLongSnackBar()
                            }
                        })
                    }
                } else {
                    if (add_name.text.toString() == "") {
                        add_name.error = getString(R.string.error_name)
                    }
                    if (add_address.text.toString() == "") {
                        add_address.error = getString(R.string.error_address)
                    }
                }
            }
            add_opening_btn -> {
                addOpeningsToRecyclerView()
            }
            opening_hours_close_edit -> {
                getTimeFromTimePicker(opening_hours_close_edit)
            }
            opening_hours_edit -> {
                getTimeFromTimePicker(opening_hours_edit)
            }
        }
    }

    /**
     * Open DatePickerDialog to let user choose a date for
     * createdDate and soldDate
     */
    private fun getTimeFromTimePicker(view: TextView) {
        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            view.text = SimpleDateFormat("HH:mm").format(calendar.time)
        }
        TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
}


    private fun checkRequiredInfo(): Boolean {
        return add_name.text.toString() != "" && add_address.text.toString() != ""
    }

    private fun addOpeningsToRecyclerView() {
        viewModel.addOpeningToRecyclerView(new_opening_day.selectedItem.toString(), opening_hours_edit.text.toString(), opening_hours_close_edit.text.toString())
            .observe(this, Observer {
                if (it == null) {
                    new_opening_hours_close.error = "Please write a closing house"
                    new_opening_hours_open.error = "Please write an opening hour"
                } else {
                    listOfOpenings.add(it)
                    openingsAdapter.setData(listOfOpenings)
                    openingsAdapter.notifyDataSetChanged()
                    clearErrorAndEditText()
                }
            })
    }

    private fun clearErrorAndEditText() {
        opening_hours_edit.text?.clear()
        new_opening_hours_open.isErrorEnabled = false
        opening_hours_close_edit.text?.clear()
        new_opening_hours_close.isErrorEnabled = false
    }

    private fun bindViews() {
        add_opening_btn.setOnClickListener(this)
        add_save_btn.setOnClickListener(this)
        opening_hours_edit.setOnClickListener(this)
        opening_hours_close_edit.setOnClickListener(this)
        new_opening_day.adapter = OpeningsAdapter(this, getListOfDay(this))
        edit_tags.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_ALL)
        openingsList = findViewById(R.id.add_opening_recycler_view)
        openingsList.layoutManager = LinearLayoutManager(this)
        openingsList.adapter = openingsAdapter
        spinnerAdapter = TypeSpinnerAdapter(this, viewModel.getTypeEnumList())
        add_edit_type_spinner.adapter = spinnerAdapter
        container = findViewById(R.id.add_activity_container)
    }

    private fun showLongSnackBar(){
        val snackBar = Snackbar.make(
            container,
            getString(R.string.establishment_succesfully_saved_snackbar),
            Snackbar.LENGTH_INDEFINITE)
        val view = snackBar.view
        val textView =
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.maxLines = 4
        snackBar.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onItemClicked(opening: String) {
        listOfOpenings.remove(opening)
        openingsAdapter.setData(listOfOpenings)
    }
}
