package com.compfest.seasalon.ui.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.compfest.seasalon.R
import com.compfest.seasalon.data.Artists

class ListArtistsAdapter(private val listArtists: List<Artists>) : RecyclerView.Adapter<ListArtistsAdapter.ListViewHolder>() {

    private var selectedItemPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artists, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listArtists.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val artist = listArtists[position]
        holder.bind(artist, position)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameListArtists)
        val experience: TextView = itemView.findViewById(R.id.textExperience)
        val rating: TextView = itemView.findViewById(R.id.textRating)
        val imgArtists: ImageView = itemView.findViewById(R.id.imgArtists)
        val checkboxSelect: ImageView = itemView.findViewById(R.id.checkboxSelect)

        fun bind(artist: Artists, position: Int) {
            imgArtists.setImageResource(artist.profile)
            name.text = artist.name
            experience.text = artist.experience
            rating.text = artist.rating

            checkboxSelect.visibility = if (selectedItemPosition == position) View.VISIBLE else View.GONE
            checkboxSelect.setImageResource(R.drawable.ic_check)

            itemView.setOnClickListener {
                val previousSelectedPosition = selectedItemPosition
                selectedItemPosition = if (selectedItemPosition == position) null else position
                notifyItemChanged(previousSelectedPosition ?: -1)
                notifyItemChanged(position)
            }
        }
    }

    // Method to get selected artist
    fun getSelectedArtist(): Artists? {
        return if (selectedItemPosition != null) listArtists[selectedItemPosition!!] else null
    }
}
