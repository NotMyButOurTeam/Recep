package com.recep.recep

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.recep.recep.data.Recipe

class RecipeViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_view)

        val recipe = intent.getParcelableExtra<Recipe>("recipe")

        val recipeToolbar = findViewById<Toolbar>(R.id.recipeViewToolbar)
        val recipeImage = findViewById<ImageView>(R.id.recipeViewImage)
        val recipeDescription = findViewById<TextView>(R.id.recipeViewDescription)

        Glide.with(this)
            .load(recipe?.previewURL)
            .onlyRetrieveFromCache(true)
            .into(recipeImage)

        recipeDescription.text = recipe?.description
        recipeToolbar.title = recipe?.name
    }
}