package com.menard.ruralis.details

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.details.comments.CommentsFragment
import com.menard.ruralis.details.contacts.ContactFragment
import com.menard.ruralis.details.fragment.InfosFragment
import com.menard.ruralis.details.photos.PhotoFragment
import java.lang.IllegalStateException

class ViewPagerAdapter constructor(manager:FragmentManager, val context: Context, private val placeDetailed: PlaceDetailed):FragmentPagerAdapter(manager) {


    override fun getItem(position: Int): Fragment {

        return when(position){
            0 -> InfosFragment.newInstance(placeDetailed)
            1 -> PhotoFragment.newInstance(placeDetailed)
            2 -> ContactFragment.newInstance(placeDetailed)
            3 -> CommentsFragment.newInstance(placeDetailed)
            else -> throw IllegalStateException("No fragment thrown from position $position")
        }
    }

    override fun getCount(): Int = DetailsCategoryEnum.values().size


    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(DetailsCategoryEnum.values()[position].title)
    }
}