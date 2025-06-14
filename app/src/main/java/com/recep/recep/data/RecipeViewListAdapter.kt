package com.recep.recep.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recep.recep.R

class RecipeViewListAdapter(private val listItems: List<String>) : RecyclerView.Adapter<RecipeViewListAdapter.ListViewHolder>() {
    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var listNumber = itemView.findViewById<TextView>(R.id.recipeItemNumber)
        var listContent = itemView.findViewById<TextView>(R.id.recipeItemContent)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_cell_list,
            parent,
            false
        )

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        var item = listItems[position]

        holder.listNumber.text = "${position + 1}."
        holder.listContent.text = item
    }

    override fun getItemCount(): Int = listItems.size
}
