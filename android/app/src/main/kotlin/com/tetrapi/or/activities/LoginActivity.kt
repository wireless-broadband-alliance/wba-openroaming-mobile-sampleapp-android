package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.tetrapi.or.databinding.ActivityLoginBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.Config
import com.tetrapi.sdk.core.SAML
import com.tetrapi.sdk.core.User
import com.tetrapi.sdk.utils.allowInfiniteLines

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
            val intent = Intent(this, ResetActivity::class.java)
            actionLauncher.launch(intent)
        }

        binding.registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            actionLauncher.launch(intent)
        }

        binding.loginButton.setOnClickListener {
            login()
        }

        val isSAMLActive = Config.isAuthActive(this, "AUTH_SAML")
        if (isSAMLActive) setSAML()
    }

    private val actionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val message = it.data?.getStringExtra("message")
            if (message != null) Snackbar.make(binding.loginButton, message, 8000).allowInfiniteLines().show()
        }
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
                Snackbar.make(binding.loginButton, message, 4000).allowInfiniteLines().show()
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
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                binding.samlButton.isReady()
                Snackbar.make(binding.samlButton, message, 4000).allowInfiniteLines().show()
            }
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
                Snackbar.make(binding.loginButton, message, 4000).allowInfiniteLines().show()
            }
        }

        user.login(
            activity = this,
            response = response
        )
    }
}