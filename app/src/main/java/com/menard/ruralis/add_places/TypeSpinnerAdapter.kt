package com.menard.ruralis.add_places

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.menard.ruralis.R

class TypeSpinnerAdapter(context: Context, list: List<TypesEnum>) : ArrayAdapter<TypesEnum>(context, R.layout.spinner_item, list){


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spinner_item, parent, false)

        val text = view.findViewById<TextView>(R.id.spinner_item_name)
        val type = getItem(position)

        text.text = context.getString(type!!.res)

        return view
    }
}