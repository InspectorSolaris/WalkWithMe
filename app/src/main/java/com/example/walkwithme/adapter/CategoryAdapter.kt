package com.example.walkwithme.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.walkwithme.R
import com.example.walkwithme.model.category.CategoryCard
import kotlinx.android.synthetic.main.category_card.view.*

class CategoryAdapter(private val categoryCardList: ArrayList<CategoryCard>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CategoryViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.category_card, p0, false)
        return CategoryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categoryCardList.size
    }

    override fun onBindViewHolder(p0: CategoryViewHolder, p1: Int) {
        p0.priority.text = categoryCardList[p1].priority.toString()
        p0.name.text = categoryCardList[p1].name
        p0.image.setImageDrawable(categoryCardList[p1].image)
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val priority: TextView = itemView.Priority
        val name: TextView = itemView.Name
        var image: ImageView = itemView.Image

        init {
            setListeners()
        }

        private fun setListeners() {
            itemView.PriorityPlusButton.setOnClickListener {
                itemView.Priority.text =
                    (minOf(itemView.Priority.text.toString().toInt() + 1, 99)).toString()
            }
            itemView.PriorityMinusButton.setOnClickListener {
                itemView.Priority.text =
                    (maxOf(itemView.Priority.text.toString().toInt() - 1, 0)).toString()
            }
        }
    }

}