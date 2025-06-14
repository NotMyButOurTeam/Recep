package com.recep.recep.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao {
    @Query("SELECT uid, name, description, ingredients, equipments, directions, imageExtension, previewURL FROM Recipe")
    fun getAllRecipes(): List<Recipe>

    @Query("SELECT uid, name, description, ingredients, equipments, directions, imageExtension, previewURL FROM Recipe LIMIT :count")
    fun getRecipes(count: Long): List<Recipe>

    @Query("SELECT id FROM Recipe WHERE uid = :uid")
    fun getRecipesByUid(uid: String): List<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveRecipes(vararg recipe: RecipeEntity)

    @Query("UPDATE Recipe SET previewURL = :previewURL WHERE uid = :uid")
    fun setRecipePreviewURL(uid: String, previewURL: String)

    @Update
    fun updateRecipe(entity: RecipeEntity)
}