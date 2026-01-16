package com.tetrapi.or.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tetrapi.or.databinding.ActivityConfigBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.Config
import com.tetrapi.sdk.core.Turnstile

//
//  ConfigActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class ConfigActivity : AppCompatActivity() {

    private val binding by lazy { ActivityConfigBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkConfig()
    }

    private fun checkConfig() {
        val turnstileKey = Turnstile.siteKey(this)
        if (turnstileKey.isNullOrBlank()) return setContentView()

        val configExpired = Config.expired(this)
        if (configExpired) return setContentView()

        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun setContentView() {
        setContentView(binding.root)
        setViews()
    }

    private fun setViews() {
        val config = Config()
        config.onInfoSuccess = {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        config.onInfoError = {

            runOnUiThread {
                binding.descriptionText.text = it ?: getString(R.string.common_error)
                binding.descriptionText.setTextColor(Color.RED)
            }
        }

        config.info(this)
    }
}