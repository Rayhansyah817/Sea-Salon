package com.compfest.seasalon.ui.reservation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.compfest.seasalon.R
import com.compfest.seasalon.data.Artists
import com.compfest.seasalon.data.ButtonTime
import com.compfest.seasalon.databinding.FragmentReservationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class ReservationFragment : Fragment() {

    private lateinit var etDate: EditText
    private val myCalendar = Calendar.getInstance()
    private lateinit var rvArtists: RecyclerView
    private lateinit var rvTimeMorning: RecyclerView
    private lateinit var spinner: Spinner

    // Properti untuk menyimpan data terpilih
    private var selectedArtist: Artists? = null
    private var selectedTime: ButtonTime? = null

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    // Firestore instance
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize RecyclerView Artists
        rvArtists = binding.rvArtists
        rvArtists.setHasFixedSize(true)
        showRecyclerListArtists()

        // Initialize RecyclerView Set Time
        rvTimeMorning = binding.rvTimeMorning
        rvTimeMorning.setHasFixedSize(true)
        showRecyclerListTime()

        // Initialize Spinner
        spinner = binding.textListMenu
        setupSpinner()

        // Initialize date picker
        etDate = binding.textTime
        datePicker()

        // Handle booking button click
        val btnBooking = binding.btnBooking
        btnBooking.setOnClickListener {
//            saveBookingToFirebase()
            showBookingSummaryDialog()
        }

        // Update booking times based on initial date
        updateBookingTimes()

        return root
    }

    // Function to list artists
    private fun listArtists(): List<Artists> {
        return listOf(
            Artists("Thomas", "7+ experience", "4.0", R.drawable.ic_artists1),
            Artists("Sekar", "10+ experience", "4.2", R.drawable.ic_artists1),
            Artists("Asep", "1+ experience", "4.1", R.drawable.ic_artists1),
            Artists("Ujang", "0+ experience", "4.8", R.drawable.ic_artists1),
        )
    }

    // Function to show artists list in RecyclerView
    private fun showRecyclerListArtists() {
        rvArtists.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val listArtistsAdapter = ListArtistsAdapter(listArtists())
        rvArtists.adapter = listArtistsAdapter
    }

    // Function to list available booking times
    private fun listTime(isToday: Boolean): List<ButtonTime> {
        val times = mutableListOf<ButtonTime>()
        val openingHour = 9
        val closingHour = 21
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        for (hour in openingHour until closingHour) {
            val time = "${String.format("%02d", hour)}.00 - ${String.format("%02d", hour + 1)}.00"
            val isActive = if (isToday) hour > currentHour else true
            val color = if (isActive) R.color.active_button_color else R.color.inactive_button_color
            times.add(ButtonTime(time, isActive, color))
        }
        return times
    }

    // Function to show booking times in RecyclerView
    private fun showRecyclerListTime() {
        rvTimeMorning.layoutManager = GridLayoutManager(context, 3)
        val isToday = myCalendar.isSameDay(Calendar.getInstance())
        val listTimeAdapter = ListTimeAdapter(listTime(isToday))
        rvTimeMorning.adapter = listTimeAdapter
    }

    // Function to setup spinner for service selection
    private fun setupSpinner() {
        val items = listOf("Haircut", "Styling", "Manicure", "Pedicure", "Facial Treatments", "Pijet+++")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Handle item selection
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection
            }
        }
    }

    // Function for date picker
    private fun datePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        etDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                dateSetListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }
    }

    // Function to update date label
    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        etDate.setText(sdf.format(myCalendar.time))
        updateBookingTimes()
    }

    // Function to update booking times
    private fun updateBookingTimes() {
        val isToday = myCalendar.isSameDay(Calendar.getInstance())
        val times = listTime(isToday)
        val listTimeAdapter = ListTimeAdapter(times)
        rvTimeMorning.adapter = listTimeAdapter
    }

    // Function to save booking data to Firestore
    private fun saveBookingToFirebase() {
        val selectedService = spinner.selectedItem.toString()
        val selectedDate = etDate.text.toString()

        // Ambil artist yang dipilih dari adapter ListArtistsAdapter
        selectedArtist = (rvArtists.adapter as? ListArtistsAdapter)?.getSelectedArtist()

        // Ambil waktu yang dipilih dari adapter ListTimeAdapter
        selectedTime = (rvTimeMorning.adapter as? ListTimeAdapter)?.getSelectedTime()

        // Cek apakah artist dan waktu sudah dipilih
        if (selectedArtist != null && selectedTime != null) {
            // Update TextView textTimeReservation dan textArtists
            binding.textTimeReservation.text = "${selectedDate}, ${selectedTime!!.time}"
            binding.textArtists.text = selectedArtist!!.name

            val bookingsRef = db.collection("bookings")
            val bookingData = hashMapOf(
                "service" to selectedService,
                "artist" to selectedArtist!!.name,
                "date" to selectedDate,
                "time" to selectedTime!!.time
                // tambahkan bidang lainnya sesuai kebutuhan
            )

            bookingsRef.add(bookingData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    // Handle successful addition
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    // Handle failure
                }
        } else {
            // Tampilkan pesan kesalahan jika artist atau waktu belum dipilih
            Log.e(TAG, "Error: Artist or time not selected")
        }
    }

    private fun generateUniqueCode(): String {
        // Generate a unique code for the booking
        return UUID.randomUUID().toString().take(8)
    }

    private fun showBookingSummaryDialog() {
        selectedArtist = (rvArtists.adapter as? ListArtistsAdapter)?.getSelectedArtist()
        selectedTime = (rvTimeMorning.adapter as? ListTimeAdapter)?.getSelectedTime()

        val selectedService = spinner.selectedItem.toString()
        val selectedDate = etDate.text.toString()
        val selectedArtistName = selectedArtist?.name ?: "No artist selected"
        val selectedTimeSlot = selectedTime?.time ?: "No time selected"

        val dialogMessage = """
        Service: $selectedService
        Artist: $selectedArtistName
        Date: $selectedDate
        Time: $selectedTimeSlot
    """.trimIndent()

        if (selectedArtist != null && selectedTime != null) {
            val alertDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogTheme)
                .setTitle("Booking Summary")
                .setMessage(dialogMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    binding.textTimeReservation.text = "$selectedDate, ${selectedTime!!.time}"
                    binding.textArtists.text = selectedArtist!!.name

                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val bookingsRef = db.collection("bookings").document(userId)
                        val bookingData = hashMapOf(
                            "userId" to userId,
                            "service" to selectedService,
                            "artist" to selectedArtist!!.name,
                            "date" to selectedDate,
                            "time" to selectedTime!!.time,
                            "uniqueCode" to generateUniqueCode()
                        )

                        bookingsRef.set(bookingData)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot successfully written!")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error writing document", e)
                            }

                        // Navigate to NotificationFragment after saving the data
                        navigateToNotificationFragment()
                    }

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()

            // Set color for the buttons
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.active_button_color))
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.inactive_button_color))
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Error")
                .setMessage("Please select an artist and a time before booking.")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            Log.e(TAG, "Error: Artist or time not selected")
        }
    }

    private fun navigateToNotificationFragment() {
        findNavController().navigate(R.id.nav_notification)
    }



//    private fun navigateToNotificationFragment(documentId: String) {
//        val bookingsRef = db.collection("bookings").document(documentId)
//        bookingsRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null && document.exists()) {
//                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                    val bundle = Bundle().apply {
//                        putString("notif_service", document.getString("service") ?: "Service not available")
//                        putString("notif_artist", document.getString("artist") ?: "Artist not available")
//                        putString("notif_date", document.getString("date") ?: "Date not available")
//                        putString("notif_time", document.getString("time") ?: "Time not available")
//                    }
//
//                    // Navigate to NotificationFragment and pass the data
//                    findNavController().navigate(R.id.nav_notification, bundle)
//                } else {
//                    Log.w(TAG, "No such document")
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error getting document", e)
//            }
//    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun Calendar.isSameDay(other: Calendar): Boolean {
    return this.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
            this.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)
}


