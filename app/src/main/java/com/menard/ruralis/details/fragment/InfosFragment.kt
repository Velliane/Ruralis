package com.menard.ruralis.details.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.menard.ruralis.R
import com.menard.ruralis.add_places.OpeningsListAdapter
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.add_places.Types
import kotlinx.android.synthetic.main.activity_add.*

class InfosFragment: Fragment() {

    /** Views */
    private lateinit var typeTextView: TextView
    private lateinit var placeDetailed: PlaceDetailed
    private lateinit var openingsRecyclerView: RecyclerView

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

        openingsRecyclerView = view.findViewById(R.id.details_openings_hours)
        openingsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        typeTextView = view.findViewById(R.id.info_title_description)
        updateViews()
        return view
    }

    private fun updateViews() {
        if(placeDetailed.fromRuralis) {
            typeTextView.text = requireContext().getString(Types.valueOf(placeDetailed.type).res)
        }else{
            typeTextView.text = placeDetailed.type
        }
        val adapter = OpeningsListAdapter(requireContext())
        openingsRecyclerView.adapter = adapter
        if(placeDetailed.openingsHours != null) {
            adapter.setData(placeDetailed.openingsHours!!)
            adapter.notifyDataSetChanged()
        }
    }


}