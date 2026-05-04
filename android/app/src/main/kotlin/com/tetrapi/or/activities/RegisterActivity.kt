package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar
import com.tetrapi.or.databinding.ActivityRegisterBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.Config
import com.tetrapi.sdk.core.User

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

        binding.termsText.setOnClickListener {
            val url = Config.tos(this)
            if (!url.isNullOrBlank()) openBrowser(url)
        }

        binding.privacyText.setOnClickListener {
            val url = Config.privacy(this)
            if (!url.isNullOrBlank()) openBrowser(url)
        }

        binding.registerButton.setOnClickListener {
            register()
        }

        binding.loginText.setOnClickListener {
            finish()
        }
    }

    private fun openBrowser(url: String){
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }

    private fun register() {
        binding.registerButton.isLoading()

        val user = User()
        user.onRegisterSuccess = {
            val message = it.getJSONObject("data").getString("message")
            setResult(RESULT_OK, Intent().apply {
                putExtra("message", message)
            })

            finish()
        }

        user.onRegisterError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                binding.registerButton.isReady()
                Snackbar.make(binding.registerButton, message, 4000).show()
            }
        }

        user.register(
            activity = this,
            email = binding.emailEdit.text.toString().trim(),
            password = binding.passwordEdit.text.toString().trim(),
            nameFirst = binding.nameFirstEdit.text.toString().trim().takeIf { it.isNotEmpty() },
            nameLast = binding.nameLastEdit.text.toString().trim().takeIf { it.isNotEmpty() },
            acceptedTOS = binding.termsCheckbox.isChecked && binding.privacyCheckbox.isChecked
        )
    }
}