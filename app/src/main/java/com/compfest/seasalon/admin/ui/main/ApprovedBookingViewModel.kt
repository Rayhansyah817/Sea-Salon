package com.compfest.seasalon.admin.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.compfest.seasalon.data.Approved
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ApprovedBookingViewModel : ViewModel() {
    private val _approvedBookings = MutableLiveData<List<Approved>>()
    val approvedBookings: LiveData<List<Approved>> get() = _approvedBookings

    private var firestoreListener: ListenerRegistration? = null

    init {
        fetchApprovedBookings()
    }

    private fun fetchApprovedBookings() {
        val db = FirebaseFirestore.getInstance()
        firestoreListener = db.collection("approved_bookings")
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    return@addSnapshotListener
                }

                val approvedList = snapshot.documents.mapNotNull { it.toObject(Approved::class.java) }
                _approvedBookings.value = approvedList
            }
    }

    override fun onCleared() {
        super.onCleared()
        firestoreListener?.remove()
    }
}
