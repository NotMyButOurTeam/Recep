package com.recep.recep

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

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

        searchBar.post {
            searchView.show()
        }

        searchView.editText.setOnEditorActionListener { _, _, _ ->
            searchBar.setText(searchView.text)
            searchView.hide()
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