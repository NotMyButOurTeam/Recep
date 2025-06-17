package com.recep.recep.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.recep.recep.R

class RecipeModifyAdapter(private val hint: String, private val listItem: MutableList<String> = mutableListOf<String>()): RecyclerView.Adapter<RecipeModifyAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var itemNumber: TextView = itemView.findViewById<TextView>(R.id.itemRecipePublishNumber)
        var itemContent: EditText = itemView.findViewById<EditText>(R.id.itemRecipePublishContent)
        var itemDelete: MaterialButton = itemView.findViewById<MaterialButton>(R.id.itemRecipePublishDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recipe_publish,
            parent,
            false
        )

        return ListViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        holder.itemNumber.text = "%d.".format(position + 1)
        holder.itemContent.hint = hint
        if (listItem[position].isNotEmpty()) {
            holder.itemContent.setText(listItem[position])
        } else {
            holder.itemContent.setText("")
        }

        holder.itemContent.addTextChangedListener({ s, start, count, after ->
        }, { s, start, before, count ->
        }, { s ->
            if (s != null) {
                listItem[position] = s.toString()
            }
        })

        holder.itemDelete.setOnClickListener {
            notifyItemRemoved(position)
            listItem.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = listItem.size

    fun itemAdd() {
        listItem.add("")
        notifyItemInserted(listItem.size - 1)
    }

    fun getContent(): String {
        var content = ""
        for (i in 0..(listItem.size - 1)) {
            content += listItem[i]
            if (i < (listItem.size - 1))
                content += "\n"
        }

        return content
    }
}