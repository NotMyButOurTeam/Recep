package com.recep.recep

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.recep.recep.components.ScreenGridLayoutManager
import com.recep.recep.database.Database
import com.recep.recep.recycler.RecipeViewAdapter

class BookmarksFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeList = view.findViewById<RecyclerView>(R.id.bookmarksRecipeList)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.bookmarksToolbar)

        recipeList.layoutManager = ScreenGridLayoutManager(requireContext(), resources)

        loadContentToRecycler(recipeList)

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.top_bookmarks_menu_item_export -> {
                }
                R.id.top_bookmarks_menu_item_import -> {
                }
                R.id.top_bookmarks_menu_item_refresh -> {
                    loadContentToRecycler(recipeList)
                }
            }
            true
        }
    }

    private fun loadContentToRecycler(recycler: RecyclerView) {
        if (isAdded) {
            val context = requireContext()
            Database.getBookmarkeds(context) { list ->
                recycler.adapter = RecipeViewAdapter(list)
            }
        }
    }
}