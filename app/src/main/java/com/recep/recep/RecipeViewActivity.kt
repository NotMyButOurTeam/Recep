package com.recep.recep

import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recep.recep.data.Recipe
import com.recep.recep.data.RecipeViewListAdapter

class RecipeViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_view)

        val recipeImage = findViewById<ImageView>(R.id.recipeViewImage)
        val recipeToolbar = findViewById<Toolbar>(R.id.recipeViewToolbar)
        val recipeDescription = findViewById<TextView>(R.id.recipeViewDescription)
        val recipeIngredients = findViewById<RecyclerView>(R.id.recipeViewIngredientsLayout)
        val recipeEquipments = findViewById<RecyclerView>(R.id.recipeViewEquipmentsLayout)
        val recipeDirections = findViewById<RecyclerView>(R.id.recipeViewDirectionsLayout)

        ViewCompat.setOnApplyWindowInsetsListener(recipeToolbar) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(
                view.paddingLeft,
                statusBarHeight,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        val recipe = intent.getParcelableExtra<Recipe>("recipe")

        Glide.with(this)
            .load(recipe?.previewURL)
            .onlyRetrieveFromCache(true)
            .into(recipeImage)

        recipeToolbar.title = recipe?.name
        recipeDescription.text = recipe?.description

        val ingredients = recipe?.ingredients?.split("\n")
        if (ingredients != null) {
            recipeIngredients.adapter = RecipeViewListAdapter(ingredients)
            recipeIngredients.layoutManager = LinearLayoutManager(this)
        }

        val equipments = recipe?.equipments?.split("\n")
        if (equipments != null) {
            recipeEquipments.adapter = RecipeViewListAdapter(equipments)
            recipeEquipments.layoutManager = LinearLayoutManager(this)
        }

        val directions = recipe?.directions?.split("\n")
        if (directions != null) {
            recipeDirections.adapter = RecipeViewListAdapter(directions)
            recipeDirections.layoutManager = LinearLayoutManager(this)
        }
    }
}