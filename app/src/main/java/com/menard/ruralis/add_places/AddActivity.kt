package com.menard.ruralis.add_places

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddViewModel::class.java)

        add_save_btn.setOnClickListener(this)
        add_photos_preview.setOnClickListener(this)
        add_photos_btn.setOnClickListener(this)
        recyclerView = findViewById(R.id.add_photos_list)

        add_edit_type_spinner.adapter = TypeSpinnerAdapter(this, viewModel.getTypeEnumList())
    }

    override fun onClick(v: View?) {
        when(v){
            add_save_btn -> {
                val name: String = add_name.text.toString()
                val type: String = add_edit_type_spinner.selectedItem.toString()
                val address: String = add_address.text.toString()
                val website: String = contact_website.text.toString()
                val phoneNumber: String = contact_phone_number.text.toString()
                viewModel.savePlace("", name, type, address, website, phoneNumber, listUri, "", "", false)
            }
            add_photos_btn -> {
                addImageToList()
            }
            add_photos_preview -> {
                selectAnImageFromPhone()
            }
        }
    }

    //-- PHOTOS --//
    private fun addImageToList() {
        if(uriPhoto != null){
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
                Glide.with(this).load(uriPhoto).into(add_photos_preview)
            }
        }
    }
}
