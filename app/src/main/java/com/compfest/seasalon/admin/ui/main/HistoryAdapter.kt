// HistoryAdapter.kt
package com.compfest.seasalon.admin.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.compfest.seasalon.data.Approved
import com.compfest.seasalon.databinding.ItemReservationHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore

class HistoryAdapter(private val bookings: MutableList<Approved>) : RecyclerView.Adapter<HistoryAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemReservationHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.bind(booking)
    }

    override fun getItemCount() = bookings.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateBookings(newBookings: List<Approved>) {
        bookings.clear()
        bookings.addAll(newBookings)
        notifyDataSetChanged()
    }

    class BookingViewHolder(private val binding: ItemReservationHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(booking: Approved) {
            binding.tvService.text = "Service : ${booking.service}"
            binding.tvArtist.text = "Artist : ${booking.artist}"
            binding.tvDate.text = "Date : ${booking.date}"
            binding.tvTime.text = "Time : ${booking.time}"
            binding.tvUniqueCode.text = "Unique Code : ${booking.uniqueCode}"

            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(booking.userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        binding.textUserId.text = "Full Name: ${document.getString("fullname")}"
                    }
                }
        }
    }
}
