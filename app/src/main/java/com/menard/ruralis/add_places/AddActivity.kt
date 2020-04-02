package com.menard.ruralis.add_places

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.menard.ruralis.R
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity(), View.OnClickListener {

    /** ViewModel */
    private lateinit var viewModel: AddViewModel

    /** List of photos */
    private var listUri = ArrayList<Uri>()
    private lateinit var uriPhoto: Uri
    private lateinit var recyclerView: RecyclerView
    private val adapter = PhotoAdapter(this)
    private lateinit var photoPreview: ImageView
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
                val name: String = add_name.text.toString()
                val type: String = add_edit_type_spinner.selectedItem.toString()
                val address: String = add_address.text.toString()
                val website: String = contact_website.text.toString()
                val phoneNumber: String = contact_phone_number.text.toString()
                viewModel.savePlace(id,
                    type, name,
                    address, listOfOpenings,
                    website, phoneNumber,
                    listUri, "", "", isEdit
                )

                if (isEdit) {
                    val intent = Intent()
                    intent.putExtra("New place id", id)
                    setResult(5, intent)
                    finish()
                }

            }
            add_photos_btn -> {
                addImageToList()
            }
            photoPreview -> {
                selectAnImageFromPhone()
            }
            add_opening_btn -> {
                addOpeningsToRecyclerView()
            }
        }
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

    //-- PHOTOS --//
    private fun addImageToList() {
        if (uriPhoto != null) {
            listUri.add(uriPhoto)
            recyclerView.adapter = adapter
            adapter.setData(listUri)
            adapter.notifyDataSetChanged()
        }
    }

    private fun selectAnImageFromPhone() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 4)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loadImageInPreview(requestCode, resultCode, data)
    }

    private fun loadImageInPreview(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {
                uriPhoto = data!!.data!!
                Glide.with(this).load(uriPhoto).into(photoPreview)
            }
        }
    }

    private fun bindViews() {
        day = findViewById(R.id.opening_day_edit)
        hours = findViewById(R.id.opening_hours_edit)
        photoPreview = findViewById(R.id.add_photos_preview)
        photoPreview.setOnClickListener(this)
        add_save_btn.setOnClickListener(this)
        add_photos_btn.setOnClickListener(this)
        add_opening_btn.setOnClickListener(this)
        recyclerView = findViewById(R.id.add_photos_list)
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
