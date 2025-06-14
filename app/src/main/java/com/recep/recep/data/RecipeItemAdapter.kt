package com.recep.recep.data

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.DiskCache
import com.recep.recep.R
import com.recep.recep.RecipeViewActivity

class RecipeItemAdapter(private val listRecipes: ArrayList<Recipe>) : RecyclerView.Adapter<RecipeItemAdapter.ListViewHolder>() {
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
        val recipe = listRecipes[position]

        holder.recipeName.text = recipe.name

        val cm  = holder.itemView.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(network)

        if (capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true) {
            RecepStorage.downloadRecipePreview(recipe)
                .addOnSuccessListener { url ->
                    Glide.with(holder.itemView.context)
                        .load(url.toString())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.recipeImage)

                    val localDb = RecepLocalDatabase(holder.itemView.context)
                    localDb.updateRecipePreviewURL(recipe.uid, url.toString())

                    Log.d("Recep", "Online Using Firebase With URL $url")
                }
        } else {
            Glide.with(holder.itemView.context)
                .load(recipe.previewURL)
                .onlyRetrieveFromCache(true)
                .into(holder.recipeImage)
            Log.d("Recep", "Offline Using Cache With URL ${recipe.previewURL}")
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