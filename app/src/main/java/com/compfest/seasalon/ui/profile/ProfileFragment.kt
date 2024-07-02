package com.compfest.seasalon.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.compfest.seasalon.databinding.FragmentProfileBinding
import com.compfest.seasalon.ui.auth.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Retrieve current user's UID
        val currentUserUid = auth.currentUser?.uid

        // Reference to the document of the current user
        val userDocRef = firestore.collection("users").document(currentUserUid!!)

        // Retrieve data from Firestore
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val fullname = document.getString("fullname")
                    val email = document.getString("email")
                    val phone = document.getString("phone")

                    // Set retrieved values to TextViews
                    binding.textFullNameProfile.text = fullname
                    binding.textEmailProfile.text = email
                    binding.textPhoneProfile.text = phone

                } else {
                    Toast.makeText(requireContext(), "Document does not exist", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching document: $e", Toast.LENGTH_SHORT).show()
            }

        // Set up the logout button
        binding.buttonLogout.setOnClickListener {
            logout()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(requireActivity(), SignInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
