package com.compfest.seasalon.ui.notification

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.compfest.seasalon.R
import com.compfest.seasalon.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null

    private val db = FirebaseFirestore.getInstance()

    private var artistId: String? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationViewModel =
            ViewModelProvider(this).get(NotificationViewModel::class.java)

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root
//
//        val textView: TextView = binding.textNotification
//        slideshowViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

//        // Get data from arguments
//        val service = arguments?.getString("notif_service") ?: "Service not available"
//        val artist = arguments?.getString("notif_artist") ?: "Artist not available"
//        val date = arguments?.getString("notif_date") ?: "Date not available"
//        val time = arguments?.getString("notif_time") ?: "Time not available"
//
//        // Set data to views
//        binding.notifServices.text = service
//        binding.notifArtist.text = artist
//        binding.notifDate.text = date
//        binding.notifTime.text = time


        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val bookingsRef = db.collection("bookings").document(userId)
            bookingsRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val service = document.getString("service") ?: "Service not available"
                        val artist = document.getString("artist") ?: "Artist not available"
                        val date = document.getString("date") ?: "Date not available"
                        val time = document.getString("time") ?: "Time not available"
                        val uniqueCode = document.getString("uniqueCode") ?: "No unique code"
                        val status = document.getString("status") ?: "Pending"
                        artistId = artist

                        // Set data to views with labels
                        binding.notifServices.text = "Service: $service"
                        binding.notifArtist.text = "Artist: $artist"
                        binding.notifDate.text = "Date: $date"
                        binding.notifTime.text = "Time: $time"
                        binding.notifUniqueCode.text = "Unique Code: $uniqueCode"
                        binding.notifStatus.text = "Status: $status"
                    } else {
                        Log.w(TAG, "No such document")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error getting document", e)
                }
        }

        binding.btnRating.setOnClickListener {
            showRatingDialog()
        }

        return root
    }

    private fun showRatingDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_rating, null)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val btnSubmitRating = dialogView.findViewById<Button>(R.id.btnSubmitRating)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        btnSubmitRating.setOnClickListener {
            val rating = ratingBar.rating
            saveRatingToFirestore(rating)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun saveRatingToFirestore(rating: Float) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val artistId = artistId

        if (userId != null && artistId != null) {
            val ratingData = hashMapOf(
                "userId" to userId,
                "artistId" to artistId,
                "rating" to rating
            )

            // Determine the correct collection for the artist
            val ratingsCollection = when (artistId) {
                "thomas" -> "ratings_thomas"
                "sekar" -> "ratings_sekar"
                "asep" -> "ratings_asep"
                "ujang" -> "ratings_ujang"
                else -> "ratings_general"
            }

            val ratingsRef = db.collection(ratingsCollection).document()

            val batch = db.batch()
            batch.set(ratingsRef, ratingData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}