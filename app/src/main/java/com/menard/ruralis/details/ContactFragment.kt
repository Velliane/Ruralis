package com.menard.ruralis.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.menard.ruralis.R
import com.menard.ruralis.add_places.Place

class ContactFragment: Fragment() {

    private lateinit var addressTxtView: TextView
    private lateinit var place: Place

    companion object{
        fun newInstance(): ContactFragment {
            return ContactFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)
        //address = arguments!!.getString("address")!!
        place = arguments!!.getSerializable("place") as Place
        addressTxtView = view.findViewById(R.id.details_address)

        updateViews()
        return view
    }

    private fun updateViews() {
        addressTxtView.text = place.address
    }
}