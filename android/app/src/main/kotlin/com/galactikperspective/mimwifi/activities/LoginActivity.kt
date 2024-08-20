package com.galactikperspective.mimwifi.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.galactikperspective.mimwifi.databinding.ActivityLoginBinding
import com.galactikperspective.or.utils.Preferences

//
//  LoginActivity.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2024 Galactik Perspective. All rights reserved.
//

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isLoggedIn = Preferences(this).getBoolean("isLoggedIn")
        if (isLoggedIn) isLoggedIn() else setContentView()
    }

    private fun isLoggedIn() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setContentView() {
        setContentView(binding.root)
        setViews()
    }

    private fun setViews() {
        binding.registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LegalActivity::class.java))
            finish()
        }
    }
}