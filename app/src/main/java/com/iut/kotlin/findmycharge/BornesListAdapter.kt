package com.iut.kotlin.findmycharge

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class BornesListAdapter (val list: List<Bornes>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflater = LayoutInflater.from(parent!!.context)
        var currentView = convertView

        if (convertView == null){
            currentView = inflater.inflate(R.layout.activity_simple_list_item, parent, false)
        }

        var tv = currentView?.findViewById<TextView>(R.id.tv_name)
        tv?.text = list[position].name

        return currentView ?: kotlin.run {
            Log.e("log", "currentView")
            View(parent.context)
        }
    }

}