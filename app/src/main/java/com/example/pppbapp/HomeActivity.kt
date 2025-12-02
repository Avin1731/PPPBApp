package com.example.pppbapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pppbapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Logic Menampilkan Nama
        if (currentUser != null) {
            // Ambil nama (jika null, gunakan string kosong)
            val userName = currentUser.displayName
            val userEmail = currentUser.email

            // Tampilkan di TextView tvWelcome
            // Jika nama ada, tampilkan nama. Jika tidak, tampilkan email
            if (!userName.isNullOrEmpty()) {
                binding.tvWelcome.text = "Halo, $userName!"
            } else {
                binding.tvWelcome.text = "Halo, $userEmail!"
            }
        } else {
            // Jika user entah bagaimana belum login, tendang ke Login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Tombol Logout
        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}