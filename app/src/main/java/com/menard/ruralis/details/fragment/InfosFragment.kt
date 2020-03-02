package com.menard.ruralis.details.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.menard.ruralis.R
import com.menard.ruralis.add_places.PlaceDetailed

class InfosFragment: Fragment() {

    /** Views */
    private lateinit var typeTextView: TextView
    private lateinit var placeDetailed: PlaceDetailed

    companion object {
        fun newInstance(place: PlaceDetailed): InfosFragment {
            val fragment = InfosFragment()
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
        val view = inflater.inflate(R.layout.fragment_infos, container, false)
        //-- Get args --//
        placeDetailed = arguments!!.getSerializable("place") as PlaceDetailed

        typeTextView = view.findViewById(R.id.info_title_description)
        updateViews()
        return view
    }

    private fun updateViews() {
        typeTextView.text = placeDetailed.type
    }


}