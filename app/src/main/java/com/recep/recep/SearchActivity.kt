package com.recep.recep

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.android.material.textview.MaterialTextView
import com.recep.recep.components.ScreenGridLayoutManager
import com.recep.recep.database.Database
import com.recep.recep.recycler.RecipeViewAdapter

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchBar = findViewById<SearchBar>(R.id.searchSearchBar)
        val searchView = findViewById<SearchView>(R.id.searchSearchView)
        val recipeList = findViewById<RecyclerView>(R.id.searchRecipeList)
        val searchNotFound = findViewById<MaterialTextView>(R.id.searchNotFound)
        val searchResult = findViewById<NestedScrollView>(R.id.searchResult)

        recipeList.layoutManager = ScreenGridLayoutManager(this, resources)

        searchBar.post {
            searchView.show()
        }

        searchView.editText.setOnEditorActionListener { _, _, _ ->
            searchBar.setText(searchView.text)
            searchView.hide()

            Database.getRecipes(this, searchView.text.toString()) { list ->
                if (list.isNotEmpty()) {
                    recipeList.adapter = RecipeViewAdapter(list)
                    searchNotFound.visibility = View.GONE
                    searchResult.visibility = View.VISIBLE
                } else {
                    searchNotFound.visibility = View.VISIBLE
                    searchResult.visibility = View.GONE
                }
            }

            false
        }

        searchView.addTransitionListener { searchView, previousState, newState ->
            if (newState == SearchView.TransitionState.HIDDEN) {
                if (searchBar.text.isEmpty()) {
                    finish()
                }
            }
        }
    }
}