package com.recep.recep.database

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import com.google.firebase.storage.FirebaseStorage
import com.recep.recep.data.Recipe

object DatabaseExternal {
    private val db: FirebaseFirestore
        get() = Firebase.firestore

    private val storage: FirebaseStorage
        get() = Firebase.storage

    fun getRecipes(count: Long, callback: (List<Recipe>) -> Unit) {
        db.collection("recipes")
            .limit(count)
            .get().addOnSuccessListener { result ->
                var list = mutableListOf<Recipe>()
                for (document in result) {
                    var recipe = Recipe(
                        uid = document.id,
                        name = document.data["name"].toString(),
                        description = document.data["description"].toString(),
                        ingredients = document.data["ingredients"].toString(),
                        equipments = document.data["equipments"].toString(),
                        directions = document.data["directions"].toString(),
                        previewURL = document.data["imageExtension"].toString()
                    )

                    list.add(recipe)
                }

                callback(list.toList())
            }
    }

    fun getRecipePreview(recipe: Recipe, callback: (String) -> Unit) {
        if (recipe.previewURL.length < 5) {
            storage.getReference("recipes_preview/${recipe.uid}.${recipe.previewURL}")
                .downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }
        }
    }
}