package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.tetrapi.or.databinding.ActivityTwofaBinding

//
//  TwoFAActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Tetrapi. All rights reserved.
//

class TwoFAActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTwofaBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.openButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://wifi.tetrapi.pt/login".toUri())
            startActivity(intent)
        }
    }
}