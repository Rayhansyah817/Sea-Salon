package com.compfest.seasalon.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.compfest.seasalon.R
import com.compfest.seasalon.databinding.FragmentSectionThreeBinding
import com.compfest.seasalon.databinding.FragmentSectionTwoBinding
import com.compfest.seasalon.ui.auth.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class SectionThreeFragment : Fragment() {

    private var _binding: FragmentSectionThreeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSectionThreeBinding.inflate(inflater, container, false)
        binding.root

        // Set up the logout button
        binding.buttonLogoutAdmin.setOnClickListener {
            logout()
        }

        return binding.root
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(requireActivity(), SignInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}