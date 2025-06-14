package com.recep.recep.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val uid: String,
    val name: String,
    val description: String,
    val ingredients: String,
    val equipments: String,
    val directions: String,
    val imageExtension: String,
    val previewURL: String
) : Parcelable