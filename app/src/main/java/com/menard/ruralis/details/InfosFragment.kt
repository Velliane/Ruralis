package com.menard.ruralis.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.menard.ruralis.R
import com.menard.ruralis.add_places.Place

class InfosFragment: Fragment() {

    /** Info */
    private lateinit var type: String
    /** Views */
    private lateinit var typeTextView: TextView
    private lateinit var place: Place

    companion object {
        fun newInstance(): InfosFragment {
            return InfosFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_infos, container, false)
        //-- Get args --//
        //type = arguments!!.getString("type")!!
        place = arguments!!.getSerializable("place") as Place

        typeTextView = view.findViewById(R.id.info_title_description)
        updateViews()
        return view
    }

    private fun updateViews() {
        typeTextView.text = place.type
    }


}