package com.recep.recep.recycler

import android.content.Intent
import android.view.LayoutInflater
import com.recep.recep.R
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.recep.recep.ViewActivity
import com.recep.recep.data.Recipe
import com.recep.recep.database.Database

class RecipeViewAdapter(val listItem: List<Recipe>): RecyclerView.Adapter<RecipeViewAdapter.ListViewHolder>() {
    class ListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val recipeView: MaterialCardView = itemView.findViewById<MaterialCardView>(R.id.itemRecipeView)
        val recipeName: TextView = itemView.findViewById<TextView>(R.id.itemRecipeViewName)
        val recipeImage: ImageView = itemView.findViewById<ImageView>(R.id.itemRecipeViewImage)
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
        if (item.previewURL.length < 5) {
            Database.updatePreview(holder.itemView.context, item) { uri ->
                Glide.with(holder.itemView.context)
                    .load(uri)
                    .into(holder.recipeImage)
                item.previewURL = uri
            }
        } else {
            Glide.with(holder.itemView.context)
                .load(item.previewURL)
                .into(holder.recipeImage)
        }

        holder.recipeView.setOnClickListener {
            val viewIntent = Intent(holder.itemView.context, ViewActivity::class.java).apply {
                putExtra("recipe", item)
            }

            holder.itemView.context.startActivity(viewIntent)
        }
    }

    override fun getItemCount(): Int = listItem.size
}