package com.menard.ruralis.details.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.menard.ruralis.R
import com.menard.ruralis.add_places.PlaceDetailed

class PhotoFragment: Fragment() {

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
        return view
    }
}