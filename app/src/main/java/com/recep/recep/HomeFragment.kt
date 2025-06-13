package com.recep.recep

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchBar
import com.recep.recep.data.RecepExternalDatabase
import com.recep.recep.data.Recipe
import com.recep.recep.data.RecipeItemAdapter

class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        RecepExternalDatabase.downloadAllRecipes().addOnSuccessListener { result ->
            if (result != null) {
                var recipesList = ArrayList<Recipe>()

                var i = 0
                val maxRecipes = 8
                for (document in result) {
                    if (i >= maxRecipes)
                        break

                    val recipe = Recipe(
                        document.id,
                        document.data["name"].toString(),
                        document.data["description"].toString(),
                        document.data["ingredients"].toString(),
                        document.data["equipments"].toString(),
                        document.data["directions"].toString(),
                        document.data["imageExtension"].toString(),
                    )

                    recipesList.add(recipe)
                    i++
                }

                if (isAdded) {
                    val recipeItemAdapter = RecipeItemAdapter(recipesList)

                    val screenWidthDp = resources.configuration.screenWidthDp
                    val itemWidthDp = 386
                    val columnCount = maxOf(1, screenWidthDp / itemWidthDp)

                    val layoutManager = GridLayoutManager(requireContext(), columnCount)

                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = recipeItemAdapter
                }
            }
        }

        val searchBar = view.findViewById<SearchBar>(R.id.searchBar)
        searchBar.setOnClickListener {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}