package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tetrapi.or.databinding.ActivityLoginBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.User

//
//  LoginActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
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

        val email = binding.emailEdit.text.toString().trim()
        val password = binding.passwordEdit.text.toString().trim()
        user.onLoginMissing2FA = {

            val intent = Intent(this, TwoFAActivity::class.java).apply {
                putExtra("uuid", email)
                putExtra("password", password)
            }

            startActivity(intent)
            finish()
        }

        user.onLoginError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        user.login(
            activity = this,
            uuid = email,
            password = password
        )
    }
}