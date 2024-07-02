package com.compfest.seasalon.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.compfest.seasalon.admin.ui.main.ApprovedBookingViewModel
import com.compfest.seasalon.admin.ui.main.HistoryAdapter
import com.compfest.seasalon.databinding.FragmentSectionTwoBinding

class SectionTwoFragment : Fragment() {
    private var _binding: FragmentSectionTwoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApprovedBookingViewModel by viewModels()
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSectionTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryAdapter(mutableListOf())
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHistory.adapter = adapter

        viewModel.approvedBookings.observe(viewLifecycleOwner, Observer { bookings ->
            adapter.updateBookings(bookings)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
