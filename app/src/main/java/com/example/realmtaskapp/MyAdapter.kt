package com.example.realmtaskapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private var mList: List<ModelDetails>,
    private val optionsMenuClickListener: OptionsMenuClickListener
) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position < mList.size) {
            var data = mList[position]
            holder.txtName.text = data.name
            holder.txtAge.text = data.age.toString()
            holder.txtCity.text = data.city
            holder.dotImg.setOnClickListener {
                optionsMenuClickListener.onOptionsMenuClicked(it, position)
            }
        }

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName = itemView.findViewById<TextView>(R.id.name)
        var txtAge = itemView.findViewById<TextView>(R.id.age)
        var txtCity = itemView.findViewById<TextView>(R.id.city)
        var dotImg=itemView.findViewById<ImageView>(R.id.img_dot)
    }

}