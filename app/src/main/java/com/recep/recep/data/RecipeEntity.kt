package com.recep.recep.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

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

    @ColumnInfo(name = "imageExtension")
    var imageExtension: String = "",

    @ColumnInfo(name = "previewURL")
    var previewURL: String = ""
) {
    @Ignore
    constructor() : this(0, "default")
}