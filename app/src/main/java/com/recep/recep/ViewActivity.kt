package com.recep.recep

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.recep.recep.data.Recipe
import com.recep.recep.database.Database
import com.recep.recep.recycler.RecipeListAdapter

class ViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recipe = intent.getParcelableExtra<Recipe>("recipe")

        val toolbar = findViewById<Toolbar>(R.id.viewToolbar)
        val image = findViewById<ImageView>(R.id.viewImage)
        val description = findViewById<TextView>(R.id.viewDescription)
        val bottomAppBar = findViewById<BottomAppBar>(R.id.viewBottomAppBar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        toolbar.title = recipe?.name
        recipe?.previewURL?.length?.let {
            if (it < 5) {
                Database.updatePreview(this, recipe) { uri ->
                    recipe.previewURL = uri

                    Glide.with(this)
                        .load(recipe.previewURL)
                        .into(image)
                }
            } else {
                Glide.with(this)
                    .load(recipe.previewURL)
                    .into(image)
            }
        }
        description.text = recipe?.description

        val ingredientList = recipe?.ingredients?.trim()?.split("\n") as List<String>
        fillList(R.id.viewIngredientList, ingredientList)

        val equipmentList = recipe.equipments.trim().split("\n")
        if (recipe.equipments.isNotEmpty()) {
            fillList(R.id.viewEquipmentList, equipmentList)
        } else {
            findViewById<TextView>(R.id.viewEquipmentTitle).visibility = View.GONE
            findViewById<RecyclerView>(R.id.viewEquipmentList).visibility = View.GONE
        }

        val directionList = recipe.directions.trim().split("\n")
        fillList(R.id.viewDirectionList, directionList)

        val bookmarkItem = bottomAppBar.menu.findItem(R.id.bottom_menu_item_bookmark)
        Database.getBookmarkStatus(this, recipe) { isBookmarked ->
            recipe.isBookmarked = isBookmarked
            updateBookmarkStatus(bookmarkItem, recipe.isBookmarked)
        }

        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.bottom_menu_item_bookmark -> {
                    recipe.isBookmarked = !recipe.isBookmarked
                    updateBookmarkStatus(bookmarkItem, recipe.isBookmarked)
                    Database.setBookmarkStatus(this, recipe, recipe.isBookmarked)
                }

                R.id.bottom_menu_item_edit -> {
                }
            }
            true
        }
    }

    private fun fillList(id: Int, list: List<String>) {
        val view = findViewById<RecyclerView>(id)
        view.layoutManager = LinearLayoutManager(this)
        view.adapter = RecipeListAdapter(list)
    }

    private fun updateBookmarkStatus(item: MenuItem, checked: Boolean) {
        if (checked) {
            item.setIcon(R.drawable.ic_bookmark_fill)
        } else {
            item.setIcon(R.drawable.ic_bookmark_outline)
        }
    }
}