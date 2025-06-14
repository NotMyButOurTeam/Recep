package com.recep.recep.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecipeEntity::class], version = 1)
private abstract class RecepDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}

class RecepLocalDatabase {
    private var db: RecepDatabase

    constructor(context: Context) {
        db = Room.databaseBuilder(
            context,
            RecepDatabase::class.java, "recep_db"
        ).allowMainThreadQueries().build()
    }

    fun getAllRecipes(): ArrayList<Recipe> {
        val list = db.recipeDao().getAllRecipes()
        return ArrayList<Recipe>(list)
    }

    fun getRecipes(count: Long): ArrayList<Recipe> {
        val list = db.recipeDao().getRecipes(count)
        return ArrayList<Recipe>(list)
    }

    fun isRecipeUidSaved(uid: String): Boolean {
        val list = db.recipeDao().getRecipesByUid(uid)
        return list.isNotEmpty()
    }

    fun saveRecipe(recipe: Recipe) {
        val entity = RecipeEntity(
            uid = recipe.uid,
            name = recipe.name,
            description = recipe.description,
            ingredients = recipe.ingredients,
            equipments = recipe.equipments,
            directions = recipe.directions,
            imageExtension = recipe.imageExtension,
            previewURL = recipe.previewURL,
        )

        db.recipeDao().saveRecipes(entity)
    }

    fun updateRecipePreviewURL(uid: String, previewURL: String) {
        if (isRecipeUidSaved(uid)) {
            db.recipeDao().setRecipePreviewURL(uid, previewURL)
        }
    }
}