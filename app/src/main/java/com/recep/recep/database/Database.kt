package com.recep.recep.database

import android.content.Context
import android.net.Uri
import android.util.Log
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

    fun getRecipe(context: Context, uid: String, callback: (Recipe) -> Unit) {
        val lifecycleOwner = context as? LifecycleOwner

        if (NetworkUtils.isNetworkAvailable(context)) {
            DatabaseExternal.getRecipe(uid) { recipe ->
                if (lifecycleOwner?.lifecycle != null) {
                    lifecycleOwner.lifecycleScope.launch {
                        insertOrUpdateInternalRecipe(recipe)
                    }
                }

                callback(recipe)
            }
        } else if (db != null) {
            if (lifecycleOwner?.lifecycle != null) {
                lifecycleOwner.lifecycleScope.launch {
                    val list = db?.recipeDao()?.getRecipe(uid)
                    if (list != null) {
                        callback(list[0])
                    }
                }
            }
        }
    }

    fun getRecipes(context: Context, count: Long, callback: (List<Recipe>) -> Unit) {
        val lifecycleOwner = context as? LifecycleOwner

        if (NetworkUtils.isNetworkAvailable(context)) {
            DatabaseExternal.getRecipes(count) { recipes ->
                if (lifecycleOwner?.lifecycle != null) {
                    lifecycleOwner.lifecycleScope.launch {
                        for (recipe in recipes) {
                            insertOrUpdateInternalRecipe(recipe)
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

    fun getRecipes(context: Context, keyword: String, callback: (List<Recipe>) -> Unit) {
        DatabaseExternal.getRecipeCount { count ->
            getRecipes(context, count.toLong()) { list ->
                val mutableList = list.toMutableList()
                for (item in list) {
                    if (!item.name.lowercase().contains(keyword.lowercase())) {
                        mutableList.remove(item)
                    }
                }

                callback(mutableList.toList())
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

    fun setBookmarkStatus(context: Context, recipe: Recipe, isBookmarked: Boolean) {
        val lifecycleOwner = context as? LifecycleOwner
        if (lifecycleOwner != null && db != null) {
            lifecycleOwner.lifecycleScope.launch {
                db?.recipeDao()?.setIsBookmarked(recipe.uid, isBookmarked)
            }
        }
    }

    fun getBookmarkStatus(context: Context, recipe: Recipe, callback: (Boolean) -> Unit) {
        val lifecycleOwner = context as? LifecycleOwner
        if (lifecycleOwner != null && db != null) {
            lifecycleOwner.lifecycleScope.launch {
                val status = db?.recipeDao()?.getIsBookmarked(recipe.uid)
                if (status != null && status.isNotEmpty()) {
                    callback(status[0])
                }
            }
        }
    }

    fun getBookmarkeds(context: Context, callback: (List<Recipe>) -> Unit) {
        val lifecycleOwner = context as? LifecycleOwner
        if (lifecycleOwner != null && db != null) {
            lifecycleOwner.lifecycleScope.launch {
                val recipes = db?.recipeDao()?.getBookmarkeds()
                if (recipes != null) {
                    callback(recipes)
                }
            }
        }
    }

    fun setRecipe(recipe: Recipe, callback: (Recipe) -> Unit) {
        DatabaseExternal.setRecipe(recipe, callback)
    }

    fun importBookmark(context: Context, uri: Uri, callback: () -> Unit) {
        val lifecycleOwner = context as? LifecycleOwner
            val stream = context.contentResolver.openInputStream(uri)
            if (stream != null) {
                val values = String(stream.readBytes())
                val recipeUids = values.split(" ")

                for (uid in recipeUids) {
                    DatabaseExternal.getRecipe(uid) { recipe ->
                        if (lifecycleOwner != null && db != null) {
                            lifecycleOwner.lifecycleScope.launch {
                                db?.recipeDao()?.setIsBookmarked(recipe.uid, true)
                            }
                        }
                    }
                }

                callback()
            }
            stream?.close()
    }

    fun exportBookmark(context: Context, uri: Uri, callback: () -> Unit) {
        val lifecycleOwner = context as? LifecycleOwner
        if (lifecycleOwner != null && db != null) {
            lifecycleOwner.lifecycleScope.launch {
                val recipes = db?.recipeDao()?.getBookmarkeds()

                val stream = context.contentResolver.openOutputStream(uri)
                if (stream != null && recipes != null) {
                    var content = ""

                    for (recipe in recipes) {
                        content += recipe.uid
                        if (recipes.last() != recipe)
                            content += " "
                    }

                    stream.write(content.toByteArray())

                    callback()
                }
                stream?.close()
            }
        }
    }

    private suspend fun insertOrUpdateInternalRecipe(recipe: Recipe) {
        val id = db?.recipeDao()?.getRecipeId(recipe.uid)

        if (!id.isNullOrEmpty()) {
            db?.recipeDao()?.updateRecipe(
                uid = recipe.uid,
                name = recipe.name,
                description = recipe.description,
                ingredients = recipe.ingredients,
                equipments = recipe.equipments,
                directions = recipe.directions,
                previewURL = recipe.previewURL
            )
        } else {
            val entity = RecipeEntity(
                uid = recipe.uid,
                name = recipe.name,
                description = recipe.description,
                ingredients = recipe.ingredients,
                equipments = recipe.equipments,
                directions = recipe.directions,
                previewURL = recipe.previewURL
            )

            db?.recipeDao()?.insertRecipe(entity)
        }
    }
}