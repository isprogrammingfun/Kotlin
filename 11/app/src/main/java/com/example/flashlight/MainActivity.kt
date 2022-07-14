package com.example.flashlight

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.flashlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.flashSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(Intent(this, TorchService::class.java).apply {
                    action = "on"
                })
            } else {
                startService(Intent(this, TorchService::class.java).apply {
                    action = "off"
                })
            }
        }
    }
}