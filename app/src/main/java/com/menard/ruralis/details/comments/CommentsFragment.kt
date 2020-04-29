package com.menard.ruralis.details.comments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.menard.ruralis.R
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.utils.Injection

class CommentsFragment : Fragment(), View.OnClickListener {

    /** PlaceDetailed get from DetailsActivity */
    private lateinit var placeDetailed: PlaceDetailed
    /** RecyclerView */
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommentsAdapter
    /** ViewModel */
    private lateinit var commentsViewModel: CommentsViewModel
    /** AddComment FAB */
    private lateinit var addCommentBtn: FloatingActionButton

    companion object {
        fun newInstance(place: PlaceDetailed): CommentsFragment {
            val fragment = CommentsFragment()
            val bundle = Bundle()
            bundle.putSerializable("place", place)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_comments, container, false)
        placeDetailed = arguments!!.getSerializable("place") as PlaceDetailed
        recyclerView = view.findViewById(R.id.fragment_comments_list)
        addCommentBtn = view.findViewById(R.id.comments_add_fab)
        addCommentBtn.setOnClickListener(this)
        if(!placeDetailed.fromRuralis){
            addCommentBtn.hide()
        }
        val viewModelFactory = Injection.provideViewModelFactory(requireContext())
        commentsViewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentsViewModel::class.java)

        adapter = CommentsAdapter(context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
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
        commentsViewModel.allCommentsLiveData.observe(this, Observer<List<Comments>> {
            adapter.setData(it)
        })
    }

    override fun onClick(v: View?) {
        val dialog = AlertDialog.Builder(context)
        val comment = EditText(requireContext())
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        comment.layoutParams = lp
        dialog.setView(comment)
        dialog.setTitle(getString(R.string.write_comments))
        dialog.setPositiveButton(getString(R.string.save)){ dialog, _ ->
            if(comment.text.toString() != "") {
                commentsViewModel.addComment(placeDetailed.placeId, comment.text.toString())
                updateViews(placeDetailed)
                dialog.dismiss()
            }
        }
        dialog.setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }
}