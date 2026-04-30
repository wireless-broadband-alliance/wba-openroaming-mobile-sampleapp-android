package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.tetrapi.or.databinding.ActivityLoginBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.Config
import com.tetrapi.sdk.core.SAML
import com.tetrapi.sdk.core.User

//
//  LoginActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class LoginActivity : ORActivity() {

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

        binding.resetText.setOnClickListener {
            startActivity(Intent(this, ResetActivity::class.java))
        }

        binding.registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            login()
        }

        val isSAMLActive = Config.isAuthActive(this, "AUTH_SAML")
        if (isSAMLActive) setSAML()
    }

    private fun login() {
        binding.loginButton.isLoading()

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
                binding.loginButton.isReady()
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        user.login(
            activity = this,
            uuid = email,
            password = password
        )
    }

    private fun setSAML() {
        binding.samlButton.visibility = View.VISIBLE
        binding.samlButton.setOnClickListener {
            startSAML()
        }
    }

    private fun startSAML() {
        binding.samlButton.isLoading()

        val saml = SAML()
        saml.onSAMLSuccess = {
            loginSAML(it)
        }

        saml.onSAMLError = {
            binding.loginButton.isReady()
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        saml.start(this)
    }

    private fun loginSAML(response: String) {
        val user = User()
        user.onLoginSuccess = {
            setMainActivity()
        }

        user.onLoginError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                binding.loginButton.isReady()
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        user.login(
            activity = this,
            response = response
        )
    }
}