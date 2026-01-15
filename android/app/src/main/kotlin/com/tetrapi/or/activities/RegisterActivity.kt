package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.tetrapi.or.databinding.ActivityRegisterBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.Config
import com.tetrapi.sdk.core.User
import kotlin.text.isNotBlank

//
//  RegisterActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Tetrapi. All rights reserved.
//

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.termsText.setOnClickListener {
            val url = Config.tos(this)
            if (url != null && url.isNotBlank()) openBrowser(url)
        }

        binding.privacyText.setOnClickListener {
            val url = Config.privacy(this)
            if (url != null && url.isNotBlank()) openBrowser(url)
        }

        binding.registerButton.setOnClickListener {
            register()
        }
    }

    private fun openBrowser(url: String){
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }

    private fun register() {
        val user = User()
        user.onRegisterSuccess = {
            val message = it.getJSONObject("data").getString("message")
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }

            finish()
        }

        user.onRegisterError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        user.register(
            this,
            email = binding.emailEdit.text.toString().trim(),
            password = binding.passwordEdit.text.toString().trim(),
            nameFirst = binding.nameFirstEdit.text.toString().trim().takeIf { it.isNotEmpty() },
            nameLast = binding.nameLastEdit.text.toString().trim().takeIf { it.isNotEmpty() },
            acceptedTerms = binding.termsCheckbox.isChecked && binding.privacyCheckbox.isChecked
        )
    }
}