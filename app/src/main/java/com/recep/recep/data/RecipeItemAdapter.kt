package com.recep.recep.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.recep.recep.R
import com.recep.recep.RecepUtils
import com.recep.recep.RecipeViewActivity
import kotlinx.coroutines.CoroutineScope

class RecipeItemAdapter(private val listRecipes: ArrayList<Recipe>, scope: CoroutineScope) : RecyclerView.Adapter<RecipeItemAdapter.ListViewHolder>() {
    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recipeName: TextView = itemView.findViewById<TextView>(R.id.recipeTitle)
        var recipeImage: ImageView = itemView.findViewById<ImageView>(R.id.recipeImage)
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
        var recipe = listRecipes[position]

        holder.recipeName.text = recipe.name

        if (RecepUtils.isNetworkAvailable(holder.itemView.context)) {
            RecepStorage.downloadRecipePreview(recipe)
                .addOnSuccessListener { url ->
                    Glide.with(holder.itemView.context)
                        .load(url.toString())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.recipeImage)

                    val localDb = RecepLocalDatabase(holder.itemView.context)
                    localDb.updateRecipePreviewURL(recipe.uid, url.toString())
                    recipe.previewURL = url.toString()
                }
        } else {
            Glide.with(holder.itemView.context)
                .load(recipe.previewURL)
                .onlyRetrieveFromCache(true)
                .into(holder.recipeImage)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RecipeViewActivity::class.java).apply {
                putExtra("recipe", recipe)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listRecipes.size
}