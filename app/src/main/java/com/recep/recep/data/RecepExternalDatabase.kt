package com.recep.recep.data

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore

object RecepExternalDatabase {
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
            "directions" to recipe.directions,
            "imageExtension" to recipe.imageExtension
        )

        return db.collection("recipes")
            .add(recipeMap)
    }

    fun updateRecipe(recipe: Recipe) {
        val recipeMap = hashMapOf(
            "name" to recipe.name,
            "description" to recipe.description,
            "ingredients" to recipe.ingredients,
            "equipments" to recipe.equipments,
            "directions" to recipe.directions,
            "imageExtension" to recipe.imageExtension
        )

        db.collection("recipes")
            .document(recipe.uid)
            .set(recipeMap)
    }
}