package com.example.pppbapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pppbapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // Variabel Firebase & Google
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // 2. Konfigurasi Google Sign In
        // Pastikan R.string.default_web_client_id sudah ada di strings.xml
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // --- SETUP LISTENER TOMBOL ---

        // Tombol Back
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Tombol Login Email/Password (Logic Standar)
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.tilEmail.error = "Isi email dulu"
            } else if (password.length < 8) {
                binding.tilEmail.error = null
                binding.tilPassword.error = "Password minimal 8 karakter"
            } else {
                binding.tilPassword.error = null
                // Untuk login email, bisa tambahkan auth.signInWithEmailAndPassword disini
                // Sementara langsung masuk Home (sesuai request)
                navigateToHome()
            }
        }

        // Link ke Register
        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 3. Tombol Google
        binding.btnGoogle.setOnClickListener {
            Log.d("GoogleLogin", "Tombol diklik. Membuka Intent Google...")
            val signInIntent = googleSignInClient.signInIntent
            launcherGoogle.launch(signInIntent)
        }
    }

    // 4. Launcher (Menangkap Hasil Pilihan Akun)
    private val launcherGoogle = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("GoogleLogin", "Result Code: ${result.resultCode}")

        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Berhasil pilih akun Google, ambil ID Token
                val account = task.getResult(ApiException::class.java)
                Log.d("GoogleLogin", "Akun Google OK: ${account.email}")

                // Lanjut Autentikasi ke Firebase
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // ERROR DISINI yang sering bikin "tidak terjadi apa-apa"
                Log.e("GoogleLogin", "Google Sign In Gagal. Code: ${e.statusCode}")
                Toast.makeText(this, "Gagal Login Google. Code: ${e.statusCode}", Toast.LENGTH_LONG).show()
            }
        } else {
            // User membatalkan login (klik tombol back saat pilih akun)
            Toast.makeText(this, "Login Dibatalkan", Toast.LENGTH_SHORT).show()
        }
    }

    // 5. Fungsi Autentikasi Firebase
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login Sukses
                    val user = auth.currentUser
                    Toast.makeText(this, "Selamat Datang, ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    // Login Gagal di sisi Firebase
                    Log.e("GoogleLogin", "Firebase Auth Gagal", task.exception)
                    Toast.makeText(this, "Auth Firebase Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }
}