package com.menard.ruralis.details.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.menard.ruralis.R
import com.menard.ruralis.add_places.PlaceDetailed
import com.menard.ruralis.utils.Constants
import kotlinx.android.synthetic.main.fragment_contact.*
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback
import java.util.*

class ContactFragment : Fragment(), View.OnClickListener {

    private lateinit var addressTxtView: TextView
    private lateinit var placeDetailed: PlaceDetailed
    private lateinit var makeCallBtn: MaterialButton
    private lateinit var visitWebsiteBtn: MaterialButton
    private lateinit var phoneNbr: TextView
    private lateinit var website: TextView
    private lateinit var map: ImageView

    companion object {
        fun newInstance(place: PlaceDetailed): ContactFragment {
            val fragment = ContactFragment()
            val bundle = Bundle()
            bundle.putSerializable("place", place)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)
        //address = arguments!!.getString("address")!!
        placeDetailed = arguments!!.getSerializable("place") as PlaceDetailed
        addressTxtView = view.findViewById(R.id.details_address)
        makeCallBtn = view.findViewById(R.id.details_make_call)
        makeCallBtn.setOnClickListener(this)
        visitWebsiteBtn = view.findViewById(R.id.details_visit_website)
        visitWebsiteBtn.setOnClickListener(this)
        phoneNbr = view.findViewById(R.id.details_phone_number)
        website = view.findViewById(R.id.details_website)
        map = view.findViewById(R.id.details_map)
        map.setOnClickListener(this)
        updateViews()
        return view
    }

    private fun updateViews() {
        addressTxtView.text = placeDetailed.address
        makeCallBtn.tag = placeDetailed.phone_number
        visitWebsiteBtn.tag = placeDetailed.website
        phoneNbr.text = placeDetailed.phone_number
        website.text = placeDetailed.website
        val key = context!!.resources.getString(R.string.api_key_google)
        val url = "https://maps.googleapis.com/maps/api/staticmap?zoom=13&size=300x300&maptype=roadmap\n" +
                "&key=$key&markers=color:blue%7Clabel:S%7C${placeDetailed.latitude},${placeDetailed.longitude}"
        Glide.with(this).load(url).into(map)
    }

    override fun onClick(view: View?) {
        when (view) {
            makeCallBtn -> {
                if (makeCallBtn.tag != null) {
                    startCall(makeCallBtn.tag.toString())
                } else {
                    showSnackBar(getString(R.string.no_phone_number))
                }
            }
            visitWebsiteBtn -> {
                if (visitWebsiteBtn.tag != null) {
                    openWebsite(visitWebsiteBtn.tag.toString())
                } else {
                    showSnackBar(getString(R.string.no_website))
                }
            }
            map -> {
                val uri = "http://maps.google.com/maps?daddr=${placeDetailed.latitude},${placeDetailed.longitude}(${placeDetailed.name})"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                context?.startActivity(intent)
            }
        }
    }

    //-- MANAGE WEBSITE --//
    private fun openWebsite(website: String) {
        val customTabsIntent = CustomTabsIntent.Builder().addDefaultShareMenuItem()
            .setToolbarColor(this.resources.getColor(R.color.colorPrimary))
            .setShowTitle(true)
            .build()
        CustomTabsHelper.addKeepAliveExtra(requireContext(), customTabsIntent.intent)
        CustomTabsHelper.openCustomTab(
            requireContext(),
            customTabsIntent,
            Uri.parse(website),
            WebViewFallback()
        )
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(view!!.findViewById(R.id.contact_container), message, Snackbar.LENGTH_SHORT)
            .show()
    }

    //-- MANAGE PHONE CALL --//
    private fun startCall(number: String) {

        if (checkPermissionForCall()) {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
            startActivity(intent)
        }

    }

    private fun checkPermissionForCall(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                Constants.REQUEST_CODE_CALL_PHONE
            )
            false
        }
    }
}