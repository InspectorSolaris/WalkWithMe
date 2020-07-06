package com.example.walkwithme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkwithme.R
import kotlinx.android.synthetic.main.stats_item.view.*

class StatsAdapter(
    private val titleList: ArrayList<String>,
    private val valueList: ArrayList<String>
) :
    RecyclerView.Adapter<StatsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.stats_item, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.title.text = titleList[p1]
        p0.value.text = valueList[p1]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.Title
        val value: TextView = itemView.Value
    }

}