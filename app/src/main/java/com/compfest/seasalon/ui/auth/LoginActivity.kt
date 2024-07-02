package com.compfest.seasalon.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.compfest.seasalon.R
import com.compfest.seasalon.ui.auth.signin.SignInActivity
import com.compfest.seasalon.ui.auth.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonSignUp = findViewById<Button>(R.id.buttonSignUpLogin)
        buttonSignUp.setOnClickListener {
            Log.d("tes","test")
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        val textSignIn = findViewById<TextView>(R.id.textSignInLogin)
        textSignIn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignInActivity::class.java))
        }

    }
}