package com.comfest.seasalon.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.compfest.seasalon.R
import com.compfest.seasalon.data.Salon
import com.compfest.seasalon.ui.reservation.ReservationFragment

class ListSalonAdapter(private val listSalon: List<Salon>) : RecyclerView.Adapter<ListSalonAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_salon_horizontal, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSalon.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, description, photo, logo) = listSalon[position]
        holder.nameSalon.text = name
        holder.descSalon.text = description
        Glide.with(holder.imgLogo.context)
            .load(logo)
            .into(holder.imgLogo)
        Glide.with(holder.imgSalon.context)
            .load(photo)
            .into(holder.imgSalon)

        // Set click listener on the itemView
        holder.itemView.setOnClickListener {
//            Toast.makeText(holder.itemView.context, "Clicked: $name", Toast.LENGTH_SHORT).show()
            val fragment = ReservationFragment()
            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment)
            fragmentTransaction.addToBackStack(null)  // Opsional: menambahkan ke back stack jika diperlukan
            fragmentTransaction.commit()
        }


    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSalon: ImageView = itemView.findViewById(R.id.imgSalon)
        val imgLogo: ImageView = itemView.findViewById(R.id.imgLogo)
        val nameSalon: TextView = itemView.findViewById(R.id.nameSalon)
        val descSalon: TextView = itemView.findViewById(R.id.descSalon)
    }
}