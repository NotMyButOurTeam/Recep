package com.recep.recep.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recep.recep.R

class RecipeListAdapter(val listItem: List<String>): RecyclerView.Adapter<RecipeListAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemNumber: TextView = itemView.findViewById<TextView>(R.id.itemRecipeListNumber)
        var itemContent: TextView = itemView.findViewById<TextView>(R.id.itemRecipeListContent)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeListAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recipe_list,
            parent,
            false
        )

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeListAdapter.ListViewHolder, position: Int) {
        holder.itemNumber.text = "%d.".format(position + 1)
        holder.itemContent.text = listItem[position]
    }

    override fun getItemCount(): Int = listItem.size
}