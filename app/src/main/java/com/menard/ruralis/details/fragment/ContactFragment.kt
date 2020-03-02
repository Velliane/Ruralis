package com.menard.ruralis.details.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.menard.ruralis.R
import com.menard.ruralis.add_places.PlaceDetailed

class ContactFragment: Fragment() {

    private lateinit var addressTxtView: TextView
    private lateinit var placeDetailed: PlaceDetailed

    companion object{
        fun newInstance(place: PlaceDetailed): ContactFragment {
            val fragment = ContactFragment()
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
        val view = inflater.inflate(R.layout.fragment_contact, container, false)
        //address = arguments!!.getString("address")!!
        placeDetailed = arguments!!.getSerializable("place") as PlaceDetailed
        addressTxtView = view.findViewById(R.id.details_address)

        updateViews()
        return view
    }

    private fun updateViews() {
        addressTxtView.text = placeDetailed.address
    }
}