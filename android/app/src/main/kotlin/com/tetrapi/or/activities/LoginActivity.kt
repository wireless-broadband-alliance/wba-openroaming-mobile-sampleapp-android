package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tetrapi.or.databinding.ActivityLoginBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.User
import androidx.core.view.isGone

//
//  LoginActivity.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Tetrapi. All rights reserved.
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

        binding.requestText.setOnClickListener {
            requestCode()
        }

        binding.configureText.setOnClickListener {
            startActivity(Intent(this, TwoFAActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            login()
        }
    }

    private fun requestCode() {
        if (binding.requestText.isGone) return
        binding.requestText.visibility = View.GONE

        val user = User()
        user.onCodeSuccess = {
            val message = it.getJSONObject("data").getString("message")
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                binding.requestText.visibility = View.VISIBLE
            }
        }

        user.onCodeError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                binding.requestText.visibility = View.VISIBLE
            }
        }

        val email = binding.emailEdit.text.toString()
        val password = binding.passwordEdit.text.toString()
        user.code(this, email, password)
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

        user.login(this,
            binding.emailEdit.text.toString(),
            binding.passwordEdit.text.toString(),
            binding.codeEdit.text.toString()
        )
    }
}