package com.compfest.seasalon.admin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.compfest.seasalon.data.Booking
import com.google.firebase.firestore.FirebaseFirestore

class ReservationViewModel : ViewModel() {
    private val _bookings = MutableLiveData<List<Booking>>()
    val bookings: LiveData<List<Booking>> get() = _bookings

    init {
        fetchBookings()
    }

    private fun fetchBookings() {
        val db = FirebaseFirestore.getInstance()
        db.collection("bookings").get()
            .addOnSuccessListener { result ->
                val bookingsList = result.map { it.toObject(Booking::class.java) }
                _bookings.value = bookingsList
            }
    }

    fun refreshBookings() {
        fetchBookings()
    }
}
