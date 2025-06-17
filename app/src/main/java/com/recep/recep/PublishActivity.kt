package com.recep.recep

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.recep.recep.recycler.RecipeModifyAdapter
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.recep.recep.data.Recipe
import com.recep.recep.database.Database

class PublishActivity : AppCompatActivity() {
    private var recipePreviewUri: Uri = Uri.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_publish)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recipeName = findViewById<TextInputEditText>(R.id.publishName)
        val recipeDescription = findViewById<TextInputEditText>(R.id.publishDescription)
        val recipeIngredientList = findViewById<RecyclerView>(R.id.publishIngredientList)
        val recipeIngredientAdd = findViewById<MaterialCardView>(R.id.publishIngredientAdd)
        val recipeEquipmentList = findViewById<RecyclerView>(R.id.publishEquipmentList)
        val recipeEquipmentAdd = findViewById<MaterialCardView>(R.id.publishEquipmentAdd)
        val recipeDirectionList = findViewById<RecyclerView>(R.id.publishDirectionList)
        val recipeDirectionAdd = findViewById<MaterialCardView>(R.id.publishDirectionAdd)
        val recipePreviewClick = findViewById<MaterialCardView>(R.id.publishPreviewClick)

        recipeIngredientList.post {
            recipeIngredientList.layoutManager = LinearLayoutManager(this)
        }
        recipeEquipmentList.post {
            recipeEquipmentList.layoutManager = LinearLayoutManager(this)
        }
        recipeDirectionList.post {
            recipeDirectionList.layoutManager = LinearLayoutManager(this)
        }

        recipeIngredientAdd.setOnClickListener {
            (recipeIngredientList.adapter as RecipeModifyAdapter).itemAdd()
        }
        recipeEquipmentAdd.setOnClickListener {
            (recipeEquipmentList.adapter as RecipeModifyAdapter).itemAdd()
        }
        recipeDirectionAdd.setOnClickListener {
            (recipeDirectionList.adapter as RecipeModifyAdapter).itemAdd()
        }

        recipePreviewClick.setOnClickListener {
            val pickerIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            startActivityForResult(pickerIntent, 0)
        }

        val topAppBar = findViewById<MaterialToolbar>(R.id.publishTopAppBar)
        topAppBar.setNavigationOnClickListener {
            finish()
        }

        val recipe = intent.getParcelableExtra<Recipe>("recipe")
        if (recipe != null && recipe.uid.isNotEmpty()) {
            updateRecipeMenuSetup(recipe, topAppBar, recipeName, recipeDescription,
                recipeIngredientList, recipeEquipmentList, recipeDirectionList)
        } else {
            newRecipeMenuSetup(topAppBar, recipeName, recipeDescription,
                recipeIngredientList, recipeEquipmentList, recipeDirectionList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            val uri = data?.data as Uri

            recipePreviewUri = uri
            findViewById<ImageView>(R.id.publishPreviewImage).setImageURI(uri)
        }
    }

    private fun updateRecipeMenuSetup(recipe: Recipe,
                                      topAppBar: MaterialToolbar,
                                      recipeName: TextInputEditText,
                                      recipeDescription: TextInputEditText,
                                      recipeIngredientList: RecyclerView,
                                      recipeEquipmentList: RecyclerView,
                                      recipeDirectionList: RecyclerView) {
        topAppBar.title = "Edit Recipe"
        recipeName.setText(recipe.name)
        recipeDescription.setText(recipe.description)

        val ingredientList = recipe.ingredients.split("\n")
        recipeIngredientList.adapter = RecipeModifyAdapter("Enter ingredient", ingredientList.toMutableList())

        val equipmentList = recipe.equipments.split("\n")
        recipeEquipmentList.adapter = RecipeModifyAdapter("Enter equipment", equipmentList.toMutableList())

        val directionList = recipe.directions.split("\n")
        recipeDirectionList.adapter = RecipeModifyAdapter("Enter direction", directionList.toMutableList())

        if (recipe.previewURL.length > 5) {
            Glide.with(this)
                .load(recipe.previewURL)
                .into(findViewById<ImageView>(R.id.publishPreviewImage))
        }

        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.top_menu_item_publish -> {
                    val recipeIngredients = (recipeIngredientList.adapter as RecipeModifyAdapter).getContent()
                    val recipeEquipments = (recipeEquipmentList.adapter as RecipeModifyAdapter).getContent()
                    val recipeDirections = (recipeDirectionList.adapter as RecipeModifyAdapter).getContent()

                    checkInputStatus(
                        recipeName.text.toString(),
                        recipeDescription.text.toString(),
                        recipeIngredients,
                        recipeDirections
                    ) {
                        Database.setRecipe(Recipe(
                            uid = recipe.uid,
                            name = recipeName.text.toString(),
                            description = recipeDescription.text.toString(),
                            ingredients = recipeIngredients,
                            equipments = recipeEquipments,
                            directions = recipeDirections,
                            previewURL = recipe.previewURL
                        )) { recipe ->
                            if (recipePreviewUri != Uri.EMPTY) {
                                Database.uploadRecipePreview(this, recipe, recipePreviewUri)
                            }
                            finish()
                        }
                    }
                }
            }
            true
        }
    }

    private fun newRecipeMenuSetup(topAppBar: MaterialToolbar,
                              recipeName: TextInputEditText,
                              recipeDescription: TextInputEditText,
                              recipeIngredientList: RecyclerView,
                              recipeEquipmentList: RecyclerView,
                              recipeDirectionList: RecyclerView) {
        recipeIngredientList.adapter = RecipeModifyAdapter("Enter ingredient...")
        recipeEquipmentList.adapter = RecipeModifyAdapter("Enter equipment...")
        recipeDirectionList.adapter = RecipeModifyAdapter("Enter direction...")
        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.top_menu_item_publish -> {
                    val recipeIngredients = (recipeIngredientList.adapter as RecipeModifyAdapter).getContent()
                    val recipeEquipments = (recipeEquipmentList.adapter as RecipeModifyAdapter).getContent()
                    val recipeDirections = (recipeDirectionList.adapter as RecipeModifyAdapter).getContent()

                    checkInputStatus(recipeName.text.toString(),
                        recipeDescription.text.toString(),
                        recipeIngredients,
                        recipeDirections) {
                        Database.uploadRecipe(Recipe(
                            uid = "",
                            name = recipeName.text.toString(),
                            description = recipeDescription.text.toString(),
                            ingredients = recipeIngredients,
                            equipments = recipeEquipments,
                            directions = recipeDirections,
                            previewURL = ""
                        )) { recipe ->
                            if (recipePreviewUri != Uri.EMPTY) {
                                Database.uploadRecipePreview(this, recipe, recipePreviewUri)
                            }
                            finish()
                        }
                    }
                }
            }
            true
        }
    }

    private fun checkInputStatus(name: String,
                                description: String,
                                ingredients: String,
                                directions: String,
                                callback: () -> Unit) {
        if (name.isNotEmpty() &&
            description.isNotEmpty() &&
            ingredients.isNotEmpty() &&
            directions.isNotEmpty()) {
            MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_publish)
                .setTitle("Do you want to publish the recipe?")
                .setMessage("Press publish to publish the recipe")
                .setPositiveButton("Publish") { dialog, which -> callback()}
                .setNegativeButton("Cancel") { dialog, which -> }
                .show()
        } else {
            MaterialAlertDialogBuilder(this)
                .setIcon(R.drawable.ic_error)
                .setTitle("Some fields are still empty")
                .setMessage("Please fill all fields before publishing the recipe.")
                .setPositiveButton("Ok") { dialog, which -> }
                .show()
        }

    }
}