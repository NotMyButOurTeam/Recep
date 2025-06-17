package com.recep.recep

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchBar
import com.recep.recep.database.Database
import com.recep.recep.recycler.RecipeViewAdapter

class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBar = view.findViewById<SearchBar>(R.id.homeSearchBar)
        val recipeList = view.findViewById<RecyclerView>(R.id.homeRecipeList)

        if (isAdded) {
            val context = requireContext()
            Database.getRecipes(context, 8) {  list ->
                val screenWidthDp = resources.configuration.screenWidthDp
                val imageWidthDp = 368
                val columnCount = maxOf(1, screenWidthDp / imageWidthDp)

                recipeList.layoutManager = GridLayoutManager(context, columnCount)
                recipeList.adapter = RecipeViewAdapter(list)
            }

            searchBar.setOnClickListener {
                val searchIntent = Intent(context, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }
    }
}