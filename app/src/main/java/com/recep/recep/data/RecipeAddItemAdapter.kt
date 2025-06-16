package com.recep.recep.data

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recep.recep.R

class RecipeAddItemAdapter(var listItems: MutableList<String>) : RecyclerView.Adapter<RecipeAddItemAdapter.ListViewHolder>() {
    class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var listNumber = itemView.findViewById<TextView>(R.id.recipeNewItemNumber)
        var listContent = itemView.findViewById<EditText>(R.id.recipeNewContent)
        var listDelete = itemView.findViewById<ImageButton>(R.id.recipeNewItemDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_cell_new_item,
            parent,
            false
        )

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        holder.listNumber.text = "${position + 1}."
        holder.listContent.setText(listItems[position])
        holder.listContent.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                listItems[position] = s.toString()
            }
        })

        holder.listDelete.setOnClickListener {
            listItems.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = listItems.size

    fun addItem() {
        listItems.add("")
        notifyItemInserted(listItems.size - 1)
    }
}
