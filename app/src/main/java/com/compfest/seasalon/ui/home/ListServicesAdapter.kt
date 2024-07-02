package com.comfest.seasalon.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.compfest.seasalon.R
import com.compfest.seasalon.data.Services

class ListServicesAdapter(private val listServices: List<Services>) : RecyclerView.Adapter<ListServicesAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_services, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listServices.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = listServices[position]
        holder.nameServices.text = currentItem.services
        holder.imgServices.setImageResource(currentItem.photo)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameServices: TextView = itemView.findViewById(R.id.nameServices)
        val imgServices: ImageView = itemView.findViewById(R.id.imgServices)
    }
}
