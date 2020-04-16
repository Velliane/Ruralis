package com.menard.ruralis.details

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.details.comments.CommentsFragment
import com.menard.ruralis.details.contacts.ContactFragment
import com.menard.ruralis.details.fragment.InfosFragment
import com.menard.ruralis.details.photos.PhotoFragment
import java.lang.IllegalStateException

class ViewPagerAdapter constructor(manager:FragmentManager, val context: Context, private var placeDetailed: PlaceDetailed):FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var infosFragment = InfosFragment()
    private var photoFragment = PhotoFragment()
    private var contactFragment = ContactFragment()
    private var commentsFragment = CommentsFragment()

    override fun getItem(position: Int): Fragment {

        return when(position){
            0 -> InfosFragment.newInstance(placeDetailed)
            1 -> PhotoFragment.newInstance(placeDetailed)
            2 -> ContactFragment.newInstance(placeDetailed)
            3 -> CommentsFragment.newInstance(placeDetailed)
            else -> throw IllegalStateException("No fragment thrown from position $position")
        }
    }

    fun refreshData(placeRefresh: PlaceDetailed){
        placeDetailed = placeRefresh
        notifyDataSetChanged()
    }


//    override fun instantiateItem(container: View, position: Int): Any {
//        val createdFragment = super.instantiateItem(container, position) as Fragment
//        when(position){
//            0 -> infosFragment = createdFragment as InfosFragment
//            1 -> photoFragment = createdFragment as PhotoFragment
//            2 -> contactFragment = createdFragment as ContactFragment
//            3 -> commentsFragment = createdFragment as CommentsFragment
//        }
//        return createdFragment
//    }

    override fun getCount(): Int = DetailsCategoryEnum.values().size

//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as View?)
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view == `object`
//    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(DetailsCategoryEnum.values()[position].title)
    }
}