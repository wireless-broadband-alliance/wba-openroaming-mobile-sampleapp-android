package com.tetrapi.or.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.tetrapi.or.databinding.ActivityTwofaBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.User
import kotlin.text.isNotEmpty

//
//  TwoFAActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Tetrapi. All rights reserved.
//

class TwoFAActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTwofaBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            login()
        }

        binding.requestText.setOnClickListener {
            requestEmailCode()
        }

        binding.totpButton.setOnClickListener {
            configureTotpCode()
        }
    }

    private fun setMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
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

        val email = intent.getStringExtra("email") ?: return finish()
        val password = intent.getStringExtra("password") ?: return finish()
        user.login(
            this,
            email = email,
            password = password,
            code = binding.twofaEdit.text.toString().trim().takeIf { it.isNotEmpty() }
        )
    }

    private fun requestEmailCode() {
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

        val email = intent.getStringExtra("email") ?: return finish()
        val password = intent.getStringExtra("password") ?: return finish()
        user.code(this, email, password, "email")
    }

    private fun configureTotpCode() {
        if (binding.totpButton.isGone) return
        binding.totpButton.visibility = View.GONE

        val user = User()
        user.onCodeSuccess = {
            val message = it.getJSONObject("data").getString("message")
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                binding.totpButton.visibility = View.VISIBLE

                val code = it.getJSONObject("data").getString("totpId")
                showTotpCode(code)
            }
        }

        user.onCodeError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                binding.totpButton.visibility = View.VISIBLE
            }
        }

        val email = intent.getStringExtra("email") ?: return finish()
        val password = intent.getStringExtra("password") ?: return finish()
        user.code(this, email, password, "totp")
    }

    private fun showTotpCode(code: String) {
        binding.totpLayout.visibility = View.VISIBLE
        binding.totpText.text = code
        binding.copyButton.setOnClickListener {
            copyToClipboard(code)
        }
    }

    fun copyToClipboard(code: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TOTP Code", code)
        clipboard.setPrimaryClip(clip)
    }
}