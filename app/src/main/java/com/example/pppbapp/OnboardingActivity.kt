package com.example.pppbapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // 1. Wajib import ini
import com.example.pppbapp.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    // Fungsi untuk berpindah halaman (bisa diakses dari fragment)
    fun setCurrentPage(page: Int) {
        binding.viewPager.currentItem = page
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 2. Pasang fungsi splash screen SEBELUM super.onCreate
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments = listOf(
            OnboardingFragment1(),
            OnboardingFragment2(),
            OnboardingFragment3()
        )
        val adapter = OnboardingFragmentAdapter(fragments, supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
    }
}