package com.menard.ruralis.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.menard.ruralis.R

class InfosFragment: Fragment() {

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
        return view
    }
}