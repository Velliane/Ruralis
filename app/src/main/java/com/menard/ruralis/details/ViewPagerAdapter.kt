package com.menard.ruralis.details

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.menard.ruralis.add_places.Place

class ViewPagerAdapter constructor(manager:FragmentManager, val context: Context, private val place: Place):FragmentPagerAdapter(manager) {


    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putSerializable("place", place)
        when(position){
            0 -> {
                val fragment = InfosFragment.newInstance()
                //bundle.putString("type", place.type)
                fragment.arguments = bundle
                return fragment
            }
            1 -> return PhotoFragment.newInstance()
            2 -> {
                val fragment = ContactFragment.newInstance()
                //bundle.putString("address", place.address)
                fragment.arguments = bundle
                return fragment
            }
        }
        return InfosFragment.newInstance()
    }

    override fun getCount(): Int = DetailsCategory.values().size


    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(DetailsCategory.values()[position].title)
    }
}