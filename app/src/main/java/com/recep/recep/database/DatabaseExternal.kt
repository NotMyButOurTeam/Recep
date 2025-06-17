package com.recep.recep.database

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
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

    fun putRecipe(recipe: Recipe, callback: (Recipe) -> Unit) {
        val item = hashMapOf(
            "name" to recipe.name,
            "description" to recipe.description,
            "ingredients" to recipe.ingredients,
            "equipments" to recipe.equipments,
            "directions" to recipe.directions,
            "imageExtension" to ""
        )

        db.collection("recipes")
            .add(item)
            .addOnSuccessListener { document ->
                callback(Recipe(
                    uid = document.id,
                    name = recipe.name,
                    description = recipe.description,
                    ingredients = recipe.ingredients,
                    equipments = recipe.equipments,
                    directions = recipe.directions,
                    previewURL = ""
                ))
            }
    }

    fun setRecipePreview(context: Context, recipe: Recipe, uri: Uri) {
        val extension = DocumentFile.fromSingleUri(context, uri)?.name?.substringAfterLast(".")
        val filename = "${recipe.uid}.$extension"

        val ref = storage.reference.child("recipes_preview/$filename")
        ref.putFile(uri).addOnSuccessListener {
            db.collection("recipes").document(recipe.uid).set(hashMapOf(
                "name" to recipe.name,
                "description" to recipe.description,
                "ingredients" to recipe.ingredients,
                "equipments" to recipe.equipments,
                "directions" to recipe.directions,
                "imageExtension" to extension
            ))
        }
    }
}