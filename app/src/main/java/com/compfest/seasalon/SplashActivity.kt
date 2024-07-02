package com.compfest.seasalon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.compfest.seasalon.ui.auth.LoginActivity
import com.compfest.seasalon.ui.auth.signin.SignInActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Tampilkan splash screen selama 1.5 detik
        val handler = Handler()
        handler.postDelayed({
            // Pindah ke MainActivity
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)

    }
}