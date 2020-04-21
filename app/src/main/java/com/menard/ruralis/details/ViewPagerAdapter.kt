package com.menard.ruralis.details

import android.content.Context
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.details.comments.CommentsFragment
import com.menard.ruralis.details.contacts.ContactFragment
import com.menard.ruralis.details.info.InfosFragment
import com.menard.ruralis.details.photos.PhotoFragment
import java.lang.IllegalStateException

class ViewPagerAdapter constructor(val context: Context, private var placeDetailed: PlaceDetailed, private val fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {

    fun refreshData(placeRefresh: PlaceDetailed){
        placeDetailed = placeRefresh
        notifyDataSetChanged()
    }

    override fun getItemCount():Int = DetailsCategoryEnum.values().size

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> InfosFragment.newInstance(placeDetailed)
            1 -> PhotoFragment.newInstance(placeDetailed)
            2 -> ContactFragment.newInstance(placeDetailed)
            3 -> CommentsFragment.newInstance(placeDetailed)
            else -> throw IllegalStateException("No fragment thrown from position $position")
        }
    }
}