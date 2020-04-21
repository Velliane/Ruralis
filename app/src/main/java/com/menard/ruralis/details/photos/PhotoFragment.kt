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
import com.menard.ruralis.R
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.utils.Injection
import kotlinx.android.synthetic.main.fragment_photo.view.*


class PhotoFragment : Fragment(), View.OnClickListener, PhotoAdapter.OnItemClickListener {

    private lateinit var viewModel: PhotosViewModel
    private lateinit var placeDetailed: PlaceDetailed
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        view.photos_add_fab.setOnClickListener(this)
        view.fragment_photos_recycler_view.layoutManager = GridLayoutManager(requireContext(), 3)

        val viewModelFactory = Injection.provideViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PhotosViewModel::class.java)
        placeDetailed = arguments?.getSerializable("place") as PlaceDetailed

        if (!placeDetailed.fromRuralis) {
            view.photos_add_fab.hide()
        }
        adapter = PhotoAdapter(requireContext(), placeDetailed.fromRuralis, this)
        view.fragment_photos_recycler_view.adapter = adapter

        searchPhotos(placeDetailed)
        viewModel.listPhotosLiveData.observe(this, Observer { list ->
            if (list == null || list.isEmpty()) {
                view.photos_no_data.visibility = View.VISIBLE
                searchPhotos(placeDetailed)
            } else {
                view.photos_no_data.visibility = View.GONE
                adapter.setData(list)
            }
        })

        return view
    }

    override fun onClick(view: View?) {
        when (view) {
            view?.photos_add_fab -> {
                selectAnImageFromPhone()
            }
        }
    }

    private fun selectAnImageFromPhone() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 4)
    }

    private fun searchPhotos(place: PlaceDetailed) {
        viewModel.getAllPhotosAccordingOrigin(
            place.fromRuralis,
            place.placeId!!,
            place.photos!!,
            requireContext()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 4) {
            val uri = data?.data
            if (uri != null) {
                viewModel.updatePhotosOfPlace(placeDetailed.placeId!!, uri.toString())
                showUploadingProgress()
            }
        }
    }

    /**
     * Show Dialog with ProgressBar while uploading image in Firebase Storage
     */
    private fun showUploadingProgress() {
        val dialog = ProgressDialog(requireContext())
        dialog.setTitle(getString(R.string.photo_uploading))
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setCancelable(false)
        dialog.show()
        viewModel.progressUploadLiveData.observe(this, Observer {
            dialog.progress = it
            if (it == 100) {
                dialog.dismiss()
                adapter.notifyDataSetChanged()
                activity?.recreate()
            }
        })
    }

    override fun onItemClicked(uri: String?) {
        viewModel.photoClicked(uri)
    }
}