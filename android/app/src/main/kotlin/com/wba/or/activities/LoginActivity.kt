package com.wba.or.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.wba.or.databinding.ActivityLoginBinding
import com.wba.sdk.R
import com.wba.sdk.config.Config
import com.wba.sdk.core.SDK
import com.wba.sdk.utils.MissingTwoFAException
import com.wba.sdk.utils.allowInfiniteLines
import kotlinx.coroutines.launch

//
//  LoginActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 WBA. All rights reserved.
//

class LoginActivity : ORActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = SDK.getUser()
        user.isLoggedIn.let {
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
            getToken()
        }

        checkConfig()
    }

    private val actionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        if (it.resultCode == RESULT_OK) {
            val message = it.data?.getStringExtra("message")
            if (message != null) Snackbar.make(binding.loginButton, message, 8000).allowInfiniteLines().show()
        }
    }

    private fun getToken() = lifecycleScope.launch {
        binding.loginButton.isLoading()

        val result = SDK.getTurnstile().getToken(
            activity = this@LoginActivity
        )

        result.onSuccess {
            login(it)
        }

        result.onFailure { error ->
            binding.loginButton.isReady()
            Snackbar.make(binding.loginButton, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }

    private fun login(token: String) = lifecycleScope.launch {
        val uuid = binding.emailEdit.text.toString().trim()
        val password = binding.passwordEdit.text.toString().trim()

        val result = SDK.getUser().login(
            uuid = uuid,
            password = password,
            token = token
        )

        result.onSuccess {
            setMainActivity()
        }

        result.onFailure { error ->
            binding.loginButton.isReady()
            when (error) {
                is MissingTwoFAException -> setTWOFaActivity(uuid, password)
                else -> Snackbar.make(binding.loginButton, error.message.toString(), 4000).allowInfiniteLines().show()
            }
        }
    }

    private fun setTWOFaActivity(uuid: String, password: String) {
        val intent = Intent(this, TwoFAActivity::class.java).apply {
            putExtra("uuid", uuid)
            putExtra("password", password)
        }

        startActivity(intent)
        finish()
    }

    private fun setSAML(config: Config) {
        binding.samlButton.visibility = if (config.isAuthActive("AUTH_SAML")) View.VISIBLE else View.GONE
        binding.samlButton.setOnClickListener {
            startSAML()
        }
    }

    private fun startSAML() = lifecycleScope.launch {
        binding.samlButton.isLoading()

        val result = SDK.getSAML().start(this@LoginActivity)
        result.onSuccess {
            loginSAML(it)
        }

        result.onFailure {
            val message = it.message ?: getString(R.string.common_error)
            runOnUiThread {
                binding.samlButton.isReady()
                Snackbar.make(binding.samlButton, message, 4000).allowInfiniteLines().show()
            }
        }
    }

    private fun loginSAML(response: String) = lifecycleScope.launch {
        val result = SDK.getUser().login(
           response = response
        )

        result.onSuccess {
            setMainActivity()
        }

        result.onFailure {
            val message = it.message ?: getString(R.string.common_error)
            runOnUiThread {
                binding.samlButton.isReady()
                Snackbar.make(binding.loginButton, message, 4000).allowInfiniteLines().show()
            }
        }
    }

    private fun checkConfig() {
        val config = SDK.getConfig()
        setSAML(config)

        if (config.isExpired) lifecycleScope.launch {
            val result = config.fetchConfig(expirationDays = 2)
            result.onSuccess {
                setSAML(config)
            }

            result.onFailure { error ->
                Snackbar.make(binding.loginButton, error.message.toString(), 4000).allowInfiniteLines().show()
            }
        }
    }
}