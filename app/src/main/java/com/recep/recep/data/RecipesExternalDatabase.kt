package com.recep.recep.data

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore

data class Recipe(
    val name: String,
    val description: String,
    val ingredients: String,
    val equipments: String,
    val directions: String
)

object RecipesExternalDatabase {
    private val db: FirebaseFirestore
        get() = Firebase.firestore

    fun downloadAllRecipes(): Task<QuerySnapshot?> {
        return db.collection("recipes").get()
    }

    fun uploadRecipe(recipe: Recipe)
    : Task<DocumentReference?> {
        val recipeMap = hashMapOf(
            "name" to recipe.name,
            "description" to recipe.description,
            "ingredients" to recipe.ingredients,
            "equipments" to recipe.equipments,
            "directions" to recipe.directions
        )

        return db.collection("recipes")
            .add(recipeMap)
    }

    fun updateRecipe(uid: String, recipe: Recipe) {
        val recipeMap = hashMapOf(
            "name" to recipe.name,
            "description" to recipe.description,
            "ingredients" to recipe.ingredients,
            "equipments" to recipe.equipments,
            "directions" to recipe.directions
        )

        db.collection("recipes")
            .document(uid)
            .set(recipeMap)
    }
}