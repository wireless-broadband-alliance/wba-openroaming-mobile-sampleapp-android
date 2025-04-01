package com.galactikperspective.mimwifi.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.galactikperspective.mimwifi.databinding.ActivityLoginBinding
import com.galactikperspective.or.R
import com.galactikperspective.or.core.User

//
//  LoginActivity.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Galactik Perspective. All rights reserved.
//

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        User.isLoggedIn(this).let {
            if (it) setMainActivity()
            else setContentView()
        }
    }

    private fun setMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setContentView() {
        setContentView(binding.root)

        binding.registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val user = User()
        user.onLoginSuccess = {
            setMainActivity()
        }

        user.onLoginError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()
        user.login(this, email, password)
    }
}