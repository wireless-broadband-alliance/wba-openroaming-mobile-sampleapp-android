package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.tetrapi.or.databinding.ActivityRegisterBinding
import com.tetrapi.sdk.core.SDK
import com.tetrapi.sdk.utils.allowInfiniteLines
import kotlinx.coroutines.launch

//
//  RegisterActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class RegisterActivity : ORActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val config = SDK.getConfig()
        binding.termsText.setOnClickListener {
            val url = config.termsOfService
            if (!url.isNullOrBlank()) openBrowser(url)
        }

        binding.privacyText.setOnClickListener {
            val url = config.privacyPolicy
            if (!url.isNullOrBlank()) openBrowser(url)
        }

        binding.registerButton.setOnClickListener {
            getToken()
        }

        binding.loginText.setOnClickListener {
            finish()
        }
    }

    private fun openBrowser(url: String){
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }

    private fun getToken() = lifecycleScope.launch {
        binding.registerButton.isLoading()

        val result = SDK.getTurnstile().getToken(
            activity = this@RegisterActivity
        )

        result.onSuccess {
            register(it)
        }

        result.onFailure { error ->
            binding.registerButton.isReady()
            Snackbar.make(binding.registerButton, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }

    private fun register(token: String) = lifecycleScope.launch {
        val result = SDK.getUser().register(
            uuid = binding.emailEdit.text.toString().trim(),
            password = binding.passwordEdit.text.toString().trim(),
            nameFirst = binding.nameFirstEdit.text.toString().trim().takeIf { it.isNotEmpty() },
            nameLast = binding.nameLastEdit.text.toString().trim().takeIf { it.isNotEmpty() },
            acceptedTOS = binding.termsCheckbox.isChecked && binding.privacyCheckbox.isChecked,
            token = token
        )

        result.onSuccess {
            setResult(RESULT_OK, Intent().apply {
                putExtra("message", it)
            })

            finish()
        }

        result.onFailure { error ->
            binding.registerButton.isReady()
            Snackbar.make(binding.registerButton, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }
}