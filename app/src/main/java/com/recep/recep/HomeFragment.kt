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
import com.recep.recep.data.RecepLocalDatabase
import com.recep.recep.data.Recipe
import com.recep.recep.data.RecipeItemAdapter

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        RecepExternalDatabase.downloadRecipes(8)
            .addOnSuccessListener { result ->
                val recipesList = RecepExternalDatabase.snapshotIntoRecipeList(result)
                if (recipesList.isNotEmpty() && isAdded) {
                    val localDb = RecepLocalDatabase(requireContext())
                    for (recipe in recipesList) {
                        if (!localDb.isRecipeUidSaved(recipe.uid))
                            localDb.saveRecipe(recipe)
                    }

                    setRecipesListContent(recipesList)
                }
            }
            .addOnFailureListener {
                val localDb = RecepLocalDatabase(requireContext())
                val recipesList = localDb.getRecipes(8)
                if (recipesList.isNotEmpty() && isAdded) {
                    setRecipesListContent(recipesList)
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

    private fun setRecipesListContent(recipesList: ArrayList<Recipe>) {
        val recipeItemAdapter = RecipeItemAdapter(recipesList)

        val screenWidthDp = resources.configuration.screenWidthDp
        val itemWidthDp = 386
        val columnCount = maxOf(1, screenWidthDp / itemWidthDp)

        val layoutManager = GridLayoutManager(requireContext(), columnCount)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recipeItemAdapter
    }
}