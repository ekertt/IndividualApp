package com.example.individualtodolist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.individualtodolist.R
import com.example.individualtodolist.models.Category
import kotlinx.android.synthetic.main.item_category.view.*

open class CategoryItemsAdapter (private val context: Context, private var list: ArrayList<Category>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )

    }
    
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            holder.itemView.tv_name.text = model.name

            holder.itemView.setOnClickListener{
                if(onClickListener!=null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }
    
    interface OnClickListener{
        fun onClick(position: Int, model: Category)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    private  class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}