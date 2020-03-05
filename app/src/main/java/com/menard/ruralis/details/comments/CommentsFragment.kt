package com.menard.ruralis.details.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.menard.ruralis.R
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.utils.Injection

class CommentsFragment : Fragment() {

    private lateinit var placeDetailed: PlaceDetailed
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentsViewModel: CommentsViewModel
    private lateinit var adapter: CommentsAdapter

    companion object {
        fun newInstance(place: PlaceDetailed): CommentsFragment {
            val fragment = CommentsFragment()
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
        val view = inflater.inflate(R.layout.fragment_comments, container, false)
        placeDetailed = arguments!!.getSerializable("place") as PlaceDetailed
        recyclerView = view.findViewById(R.id.fragment_comments_list)


        val viewModelFactory = Injection.provideViewModelFactory()
        commentsViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CommentsViewModel::class.java)

        adapter = CommentsAdapter(requireContext())
        recyclerView.adapter = adapter
        updateViews(placeDetailed)
        return view
    }

    private fun updateViews(place: PlaceDetailed) {
        commentsViewModel.getCommentsOfPlace(
            place.placeId,
            place.fromRuralis,
            requireContext().getString(R.string.details_field),
            requireContext().getString(R.string.api_key_google)
        )
        commentsViewModel.commentsLiveData.observe(this, Observer<List<Comments>> {
            adapter.setData(it)
            adapter.notifyDataSetChanged()
        })
    }
}