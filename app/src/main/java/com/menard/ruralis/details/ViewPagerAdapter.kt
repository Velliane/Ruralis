package com.menard.ruralis.details

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class ViewPagerAdapter constructor(manager:FragmentManager, val context: Context):FragmentPagerAdapter(manager) {


    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return InfosFragment.newInstance()
            1 -> return PhotoFragment.newInstance()
        }
        return InfosFragment.newInstance()
    }

    override fun getCount(): Int = DetailsCategory.values().size


    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(DetailsCategory.values()[position].title)
    }
}