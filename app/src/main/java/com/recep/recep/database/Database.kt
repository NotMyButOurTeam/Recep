package com.recep.recep.database

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.recep.recep.data.Recipe
import com.recep.recep.utils.NetworkUtils
import kotlinx.coroutines.launch

object Database {
    var db: DatabaseInternal? = null

    fun initLocal(context: Context) {
        db = Room.databaseBuilder(
            context,
            DatabaseInternal::class.java, "recep_db"
        ).build()
    }

    fun getRecipes(context: Context, count: Long, callback: (List<Recipe>) -> Unit) {
        val lifecycleOwner = context as? LifecycleOwner

        if (NetworkUtils.isNetworkAvailable(context)) {
            DatabaseExternal.getRecipes(count) { recipes ->
                if (lifecycleOwner?.lifecycle != null) {
                    lifecycleOwner.lifecycleScope.launch {
                        for (recipe in recipes) {
                            val id = db?.recipeDao()?.getRecipeId(recipe.uid)
                            if (id != null && id.isNotEmpty()) {
                                val item = RecipeEntity(
                                    id = id[0],
                                    uid = recipe.uid,
                                    name = recipe.name,
                                    description = recipe.description,
                                    ingredients = recipe.ingredients,
                                    equipments = recipe.equipments,
                                    directions = recipe.directions,
                                    previewURL = recipe.previewURL
                                )
                                db?.recipeDao()?.updateRecipe(item)
                            } else if (id == null || id?.isEmpty() == true) {
                                val item = RecipeEntity(
                                    uid = recipe.uid,
                                    name = recipe.name,
                                    description = recipe.description,
                                    ingredients = recipe.ingredients,
                                    equipments = recipe.equipments,
                                    directions = recipe.directions,
                                    previewURL = recipe.previewURL
                                )

                                db?.recipeDao()?.insertRecipe(item)
                            }
                        }
                    }
                }

                callback(recipes)
            }
        } else if (db != null) {
            if (lifecycleOwner?.lifecycle != null) {
                lifecycleOwner.lifecycleScope.launch {
                    val list = db?.recipeDao()?.getRecipes(count)
                    if (list != null) {
                        callback(list)
                    }
                }
            }
        }
    }

    fun updatePreviews(context: Context, recipes: List<Recipe>, callback: (String) -> Unit) {
        for (recipe in recipes) {
            updatePreview(context, recipe, callback)
        }
    }

    fun updatePreview(context: Context, recipe: Recipe, callback: (String) -> Unit) {
        val lifecycleOwner = context as? LifecycleOwner

        if (NetworkUtils.isNetworkAvailable(context)) {
            DatabaseExternal.getRecipePreview(recipe) { uri ->
                if (lifecycleOwner?.lifecycle != null) {
                    lifecycleOwner.lifecycleScope.launch {
                        db?.recipeDao()?.updateRecipePreview(recipe.uid, uri)
                    }
                }

                if (uri.isNotEmpty())
                    callback(uri)
            }
        } else if (db != null) {
            if (lifecycleOwner?.lifecycle != null) {
                lifecycleOwner.lifecycleScope.launch {
                    val previewURL = db?.recipeDao()?.getRecipePreview(recipe.uid)
                    if (previewURL != null && previewURL.isNotEmpty()) {
                        callback(previewURL[0])
                    }
                }
            }
        }
    }

    fun uploadRecipe(recipe: Recipe, callback: (Recipe) -> Unit) {
        DatabaseExternal.putRecipe(recipe, callback)
    }

    fun uploadRecipePreview(context: Context, recipe: Recipe, uri: Uri) {
        DatabaseExternal.setRecipePreview(context, recipe, uri)
    }
}