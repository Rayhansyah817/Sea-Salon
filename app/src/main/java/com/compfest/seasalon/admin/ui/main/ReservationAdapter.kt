// ReservationAdapter.kt
package com.compfest.seasalon.admin.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.compfest.seasalon.data.Booking
import com.compfest.seasalon.databinding.ItemReservationBinding
import com.google.firebase.firestore.FirebaseFirestore

class ReservationAdapter(private val bookings: MutableList<Booking>) : RecyclerView.Adapter<ReservationAdapter.BookingViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun updateBookings(newBookings: List<Booking>) {
        bookings.clear()
        bookings.addAll(newBookings)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.bind(booking)
    }

    override fun getItemCount() = bookings.size

    class BookingViewHolder(
        private val binding: ItemReservationBinding,
        private val onItemClickListener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: Booking) {
            binding.tvService.text = "Service: ${booking.service}"
            binding.tvArtist.text = "Artist: ${booking.artist}"
            binding.tvDate.text = "Date: ${booking.date}"
            binding.tvTime.text = "Time: ${booking.time}"

            binding.btnApprove.setOnClickListener {
                // Approve booking
                val db = FirebaseFirestore.getInstance()
                val approvedBooking = hashMapOf(
                    "service" to booking.service,
                    "artist" to booking.artist,
                    "date" to booking.date,
                    "time" to booking.time,
                    "userId" to booking.userId,
                    "uniqueCode" to booking.uniqueCode // Function to generate unique code
                )
                db.collection("approved_bookings").add(approvedBooking)
                    .addOnSuccessListener {
                        // Remove booking from pending bookings
                        onItemClickListener?.onItemClicked()
                        Log.d("ReservationAdapter", "Booking approved and item clicked callback called")
                        db.collection("bookings").document(booking.userId).delete()
                    }
            }
        }
    }
}
