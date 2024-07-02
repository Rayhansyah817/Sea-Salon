// SectionOneFragment.kt
package com.compfest.seasalon.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.compfest.seasalon.admin.ui.main.ReservationAdapter
import com.compfest.seasalon.admin.ui.main.ReservationViewModel
import com.compfest.seasalon.data.Booking
import com.compfest.seasalon.databinding.FragmentSectionOneBinding

class SectionOneFragment : Fragment(), ReservationAdapter.OnItemClickListener {

    private var _binding: FragmentSectionOneBinding? = null
    private val binding get() = _binding!!

    private val bookingViewModel: ReservationViewModel by viewModels()
    private lateinit var adapter: ReservationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSectionOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ReservationAdapter(mutableListOf())
        adapter.setOnItemClickListener(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        bookingViewModel.bookings.observe(viewLifecycleOwner, Observer { bookings ->
            adapter.updateBookings(bookings)
        })
    }

    override fun onItemClicked() {
        Log.d("SectionOneFragment", "Item clicked, refreshing bookings")
        bookingViewModel.refreshBookings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
