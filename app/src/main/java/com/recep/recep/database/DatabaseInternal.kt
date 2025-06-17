package com.recep.recep.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.recep.recep.data.Recipe

@Entity(tableName = "Recipe")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "uid")
    var uid: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "ingredients")
    var ingredients: String = "",

    @ColumnInfo(name = "equipments")
    var equipments: String = "",

    @ColumnInfo(name = "directions")
    var directions: String = "",

    @ColumnInfo(name = "previewURL")
    var previewURL: String = "",

    @ColumnInfo(name = "isBookmarked")
    var isBookmarked: Boolean = false
) {
    @Ignore
    constructor() : this(0, "default")
}

@Dao
interface RecipeDao {
    @Query("SELECT $RECIPE_ATTRIBUTES FROM Recipe LIMIT :count")
    suspend fun getRecipes(count: Long): List<Recipe>

    @Query("SELECT $RECIPE_ATTRIBUTES FROM Recipe WHERE uid = :uid")
    suspend fun getRecipe(uid: String): List<Recipe>

    @Query("SELECT previewURL FROM Recipe WHERE uid = :uid")
    suspend fun getRecipePreview(uid: String): List<String>

    @Query("SELECT id FROM Recipe WHERE uid = :uid")
    suspend fun getRecipeId(uid: String): List<Int>

    @Query("UPDATE Recipe SET name = :name, description = :description, ingredients = :ingredients, equipments = :equipments, directions = :directions, previewURL = :previewURL WHERE uid = :uid")
    suspend fun updateRecipe(uid: String,
                             name: String,
                             description: String,
                             ingredients: String,
                             equipments: String,
                             directions: String,
                             previewURL: String)

    @Query("UPDATE Recipe SET previewURL = :url WHERE uid = :uid")
    suspend fun updateRecipePreview(uid: String, url: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRecipe(vararg recipes: RecipeEntity)

    @Query("UPDATE Recipe SET isBookmarked = :isBookmarked WHERE uid = :uid")
    suspend fun setIsBookmarked(uid: String, isBookmarked: Boolean)

    @Query("SELECT isBookmarked FROM Recipe WHERE uid = :uid")
    suspend fun getIsBookmarked(uid: String): List<Boolean>

    @Query("SELECT $RECIPE_ATTRIBUTES FROM Recipe WHERE isBookmarked = 1 ORDER BY name")
    suspend fun getBookmarkeds(): List<Recipe>

    companion object {
        const val RECIPE_ATTRIBUTES = "uid, name, description, ingredients, equipments, directions, previewURL, isBookmarked"
    }
}

@Database(entities = [RecipeEntity::class], version = 1)
abstract class DatabaseInternal: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}