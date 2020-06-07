package com.example.individualtodolist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.individualtodolist.R
import com.example.individualtodolist.models.ToDo
import kotlinx.android.synthetic.main.item_todo.view.*

open class ToDoListItemsAdapter(
    private val context: Context,
    private var list: List<ToDo>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_todo_list_title.text = model.title
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}