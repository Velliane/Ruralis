package com.menard.ruralis.details.photos

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.menard.ruralis.R
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.utils.Injection


class PhotoFragment: Fragment(), View.OnClickListener, PhotoAdapter.OnItemClickListener {

    private lateinit var addPhotoBtn: FloatingActionButton
    private lateinit var viewModel: PhotosViewModel
    private lateinit var placeDetailed: PlaceDetailed
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotoAdapter

    companion object {
        fun newInstance(place: PlaceDetailed): PhotoFragment {
            val fragment = PhotoFragment()
            val bundle = Bundle()
            bundle.putSerializable("place", place)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)

        val viewModelFactory = Injection.provideViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PhotosViewModel::class.java)
        placeDetailed = arguments?.getSerializable("place") as PlaceDetailed
        addPhotoBtn = view.findViewById(R.id.photos_add_fab)
        addPhotoBtn.setOnClickListener(this)
        if(!placeDetailed.fromRuralis) {
           addPhotoBtn.hide()
        }
        recyclerView = view.findViewById(R.id.fragment_photos_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = PhotoAdapter(requireContext(), placeDetailed.fromRuralis, this)
        recyclerView.adapter = adapter

        viewModel.getAllPhotosAccordingOrigin(placeDetailed.fromRuralis, placeDetailed.placeId!!, placeDetailed.photos)
        viewModel.listPhotosLiveData.observe(this, Observer {
            adapter.setData(it)
        })
        return view
    }

    override fun onClick(view: View?) {
        when(view){
            addPhotoBtn -> {selectAnImageFromPhone()}
        }
    }

    private fun selectAnImageFromPhone() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 4)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 4){
            val uri = data?.data
            viewModel.updatePhotosOfPlace(placeDetailed.placeId!!, uri.toString())
            showUploadingProgress()
        }
    }

    /**
     * Show Dialog with ProgressBar while uploading image in Firebase Storage
     */
    private fun showUploadingProgress(){
        val dialog = ProgressDialog(requireContext())
        dialog.setTitle(getString(R.string.photo_uploading))
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.show()
        viewModel.progressUploadLiveData.observe(this, Observer {
            dialog.progress = it
            if(it == 100){
                dialog.dismiss()
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onItemClicked(id: String?) {
        viewModel.photoClicked(id)
    }
}