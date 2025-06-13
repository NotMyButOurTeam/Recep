package com.recep.recep.data

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

object RecepStorage {
    private val storage: FirebaseStorage
        get() = Firebase.storage

    fun downloadRecipePreview(recipe: Recipe): Task<Uri> {
        return storage.getReference("recipes_preview/${recipe.uid}.${recipe.imageExtension}").downloadUrl
    }
}