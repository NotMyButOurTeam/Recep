package com.recep.recep.recycler

import android.view.LayoutInflater
import com.recep.recep.R
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.recep.recep.data.Recipe
import com.recep.recep.database.Database

class RecipeViewAdapter(val listItem: List<Recipe>): RecyclerView.Adapter<RecipeViewAdapter.ListViewHolder>() {
    class ListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val recipeName = itemView.findViewById<TextView>(R.id.itemRecipeViewName)
        val recipeImage = itemView.findViewById<ImageView>(R.id.itemRecipeViewImage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recipe_view,
            parent,
            false
        )

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        val item = listItem[position]

        holder.recipeName.text = item.name
        Database.updatePreview(holder.itemView.context, item) { uri ->
            Glide.with(holder.itemView.context)
                .load(uri)
                .into(holder.recipeImage)
        }
    }

    override fun getItemCount(): Int = listItem.size

    fun refereshDataset() {
        notifyDataSetChanged()
    }
}