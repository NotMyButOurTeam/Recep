package com.recep.recep.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recep.recep.R

class RecipeItemAdapter(private val listRecipes: ArrayList<Recipe>) : RecyclerView.Adapter<RecipeItemAdapter.ListViewHolder>() {
    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recipeName = itemView.findViewById<TextView>(R.id.recipeTitle)
        var recipeImage = itemView.findViewById<ImageView>(R.id.recipeImage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_cell_recipe,
            parent,
            false
        )

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        val recipe = listRecipes[position]

        holder.recipeName.text = recipe.name

        RecepStorage.downloadRecipePreview(recipe.previewURL)
            .addOnSuccessListener { url ->
                Glide.with(holder.itemView.context)
                    .load(url)
                    .into(holder.recipeImage)
            }
    }

    override fun getItemCount(): Int = listRecipes.size
}