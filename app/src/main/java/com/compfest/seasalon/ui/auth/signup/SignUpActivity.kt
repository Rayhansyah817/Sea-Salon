package com.compfest.seasalon.ui.auth.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.compfest.seasalon.MainActivity
import com.compfest.seasalon.ui.auth.signin.SignInActivity
import com.compfest.seasalon.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val etFullName = binding.fullnamesignup
        val etPhone = binding.phoneSignup
        val etEmail = binding.emailsignup
        val etPassword = binding.passwordsignup
        val btnSignUp = binding.buttonSignUp
        val progressBar = binding.progressBar

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        btnSignUp.setOnClickListener {
            val fullname = etFullName.text.toString()
            val phone = etPhone.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (fullname.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Ada Data Yang Masih Kosong!!", Toast.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = android.view.View.VISIBLE
                // Create a new user with email and password
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        progressBar.visibility = android.view.View.GONE
                        if (task.isSuccessful) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d("SignUpActivity", "createUserWithEmail:success")

                            // Simpan data pengguna ke Firestore
                            val user = auth.currentUser
                            val userDocument = firestore.collection("users").document(user!!.uid)
                            val userData = hashMapOf(
                                "fullname" to fullname,
                                "phone" to phone,
                                "email" to email
                                // Tambahkan data lain sesuai kebutuhan
                            )

                            userDocument.set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Authentication successful.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    updateUI(user)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("SignUpActivity", "Error adding document", e)
                                    Toast.makeText(
                                        this,
                                        "Firestore error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    updateUI(null)
                                }
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                this,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }
                    }
            }
        }


        val textLoginSignUp = binding.textloginsignup
        textLoginSignUp.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
        }
    }

    private fun reload() {
        // Implement your logic to reload the user session or refresh the UI
    }
}
