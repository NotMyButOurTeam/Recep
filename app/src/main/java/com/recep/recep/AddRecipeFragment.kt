package com.recep.recep

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recep.recep.data.RecipeAddItemAdapter

class AddRecipeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var listIngredients = mutableListOf<String>()
        var listEquipments = mutableListOf<String>()
        var listDirections = mutableListOf<String>()

        val recipeImage = view.findViewById<ImageView>(R.id.recipeAddImage)
        val recipeName = view.findViewById<EditText>(R.id.recipeAddName)
        val recipeDescription = view.findViewById<EditText>(R.id.recipeAddDescription)
        val recipeAddIngredient = view.findViewById<Button>(R.id.recipeAddNewIngredient)
        val recipeAddEquipment = view.findViewById<Button>(R.id.recipeAddNewEquipment)
        val recipeAddDirection = view.findViewById<Button>(R.id.recipeAddNewDirection)
        val recipeIngredients = view.findViewById<RecyclerView>(R.id.recipeAddIngredients)
        val recipeEquipments = view.findViewById<RecyclerView>(R.id.recipeAddEquipments)
        val recipeDirections = view.findViewById<RecyclerView>(R.id.recipeAddDirections)
        val recipePublish = view.findViewById<Button>(R.id.recipeAddPublish)

        recipeImage.setOnClickListener {

        }

        if (isAdded) {
            val ingredientAdapter = RecipeAddItemAdapter(listIngredients)
            recipeIngredients.layoutManager = LinearLayoutManager(requireContext())
            recipeIngredients.adapter = ingredientAdapter
            recipeAddIngredient.setOnClickListener {
                ingredientAdapter.addItem()
            }

            val equipmentAdapter = RecipeAddItemAdapter(listEquipments)
            recipeEquipments.layoutManager = LinearLayoutManager(requireContext())
            recipeEquipments.adapter = equipmentAdapter
            recipeAddEquipment.setOnClickListener {
                equipmentAdapter.addItem()
            }

            val directionAdapter = RecipeAddItemAdapter(listDirections)
            recipeDirections.layoutManager = LinearLayoutManager(requireContext())
            recipeDirections.adapter = directionAdapter
            recipeAddDirection.setOnClickListener {
                directionAdapter.addItem()
            }
        }

        recipePublish.setOnClickListener {
        }
    }
}