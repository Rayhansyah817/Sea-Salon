package com.compfest.seasalon.ui.auth.signin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.compfest.seasalon.ui.auth.signup.SignUpActivity
import com.compfest.seasalon.MainActivity
import com.compfest.seasalon.admin.AdminActivity
import com.compfest.seasalon.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  // Call super first
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val etEmail = binding.emailsignin
        val etPassword = binding.passwordsignin
        val btnSignIn = binding.btnsignin

        auth = FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email or Password is empty!", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            if (user != null) {
                                checkIfAdmin(user)
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                this,
                                "Login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT,
                            ).show()
                            updateUI(null)
                        }
                    }
            }
        }

        val textSignUp = binding.textSignUp
        textSignUp.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            checkIfAdmin(currentUser)
        }
    }

    private fun checkIfAdmin(user: FirebaseUser) {
        val uid = user.uid
        val db = FirebaseFirestore.getInstance()

        // Check if the user is an admin
        val docRef = db.collection("admins").document(uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(this, "Admin Login Seccessful", Toast.LENGTH_SHORT).show()
                    navigateToAdminsUI()
                } else {
                    Toast.makeText(this, "User Login Seccessful", Toast.LENGTH_SHORT).show()
                    navigateToUsersUI()
                }
        }
            .addOnFailureListener() { exception ->
                Log.d(TAG, "get failed with ", exception)
                Toast.makeText(this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToUsersUI() {
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()  // Finish the current activity to prevent back navigation to the sign-in screen
    }

    private fun navigateToAdminsUI() {
        startActivity(Intent(this@SignInActivity, AdminActivity::class.java))
        finish()  // Finish the current activity to prevent back navigation to the sign-in screen
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            checkIfAdmin(user)
        }
    }
}
