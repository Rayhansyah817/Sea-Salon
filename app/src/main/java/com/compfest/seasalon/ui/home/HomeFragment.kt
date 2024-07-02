package com.compfest.seasalon.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comfest.seasalon.ui.home.HomeViewModel
import com.comfest.seasalon.ui.home.ListSalonAdapter
import com.comfest.seasalon.ui.home.ListServicesAdapter
import com.compfest.seasalon.R
import com.compfest.seasalon.data.Salon
import com.compfest.seasalon.data.Services
import com.compfest.seasalon.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var rvSalon: RecyclerView
    private lateinit var rvServices: RecyclerView

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize RecyclerView for Salon
        rvSalon = binding.rvSalon
        rvSalon.setHasFixedSize(true)
        rvSalon.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        showRecyclerListSalon()

        // Initialize RecyclerView for Services
        rvServices = binding.rvServices
        rvServices.setHasFixedSize(true)
        rvServices.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        showRecyclerListServices()

        return root
    }

    // Services
    private fun listServices(): List<Services> {
        return listOf(
            Services(R.drawable.ic_haircut, "Haircut"),
            Services(R.drawable.ic_styling, "Styling"),
            Services(R.drawable.ic_manicure, "Manicure"),
            Services(R.drawable.ic_pedicure, "Pedicure"),
            Services(R.drawable.ic_facial, "Facial Treatments")
        )
    }

    private fun showRecyclerListServices() {
        rvServices.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val listServicesAdapter = ListServicesAdapter(listServices())
        rvServices.adapter = listServicesAdapter
    }

    // Salon
    private fun listSalon(): List<Salon> {
        return listOf(
            Salon("Asgar Salon",
                "Haircut & Pijat++",
                "https://raw.githubusercontent.com/Rayhansyah817/ic_salon/main/salon1.png",
                "https://raw.githubusercontent.com/Rayhansyah817/ic_salon/main/ic_salon1.png"),
            Salon("Divia Style Salon",
                "Manicure &amp; Pedicure",
                "https://raw.githubusercontent.com/Rayhansyah817/ic_salon/main/salon2.png",
                "https://raw.githubusercontent.com/Rayhansyah817/ic_salon/main/ic_salon2.png"),
            Salon("Jawa Style Salon",
                "Hair Style Salon",
                "https://raw.githubusercontent.com/Rayhansyah817/ic_salon/main/salon3.png",
                "https://raw.githubusercontent.com/Rayhansyah817/ic_salon/main/ic_salon3.png")
        )
    }

    private fun showRecyclerListSalon() {
        rvSalon.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val listSalonAdapter = ListSalonAdapter(listSalon())
        rvSalon.adapter = listSalonAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
