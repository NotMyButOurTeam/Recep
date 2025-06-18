package com.recep.recep

import android.app.Activity
import android.app.Activity.*
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
    companion object {
        const val REQUEST_CREATE_FILE = 0
        const val REQUEST_OPEN_FILE = 1
    }

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
                    val exportIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        type="application/octet-stream"

                        putExtra(Intent.EXTRA_TITLE, "bookmarks.rbf")
                    }

                    startActivityForResult(exportIntent, REQUEST_CREATE_FILE)
                }
                R.id.top_bookmarks_menu_item_import -> {
                    val importIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        type="application/octet-stream"
                    }

                    startActivityForResult(importIntent, REQUEST_OPEN_FILE)
                }
                R.id.top_bookmarks_menu_item_refresh -> {
                    loadContentToRecycler(recipeList)
                }
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_OPEN_FILE -> {
                    val uri = data?.data as Uri
                    val context = context
                    if (context != null)
                        Database.importBookmark(context, uri) {
                        }
                }
                REQUEST_CREATE_FILE -> {
                    val uri = data?.data as Uri
                    val context = context
                    if (context != null)
                        Database.exportBookmark(context, uri) {
                        }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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