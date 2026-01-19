package com.tetrapi.or.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tetrapi.sdk.R
import com.tetrapi.or.databinding.ActivityTwofaConfigurationBinding
import com.tetrapi.sdk.core.User

//
//  TwoFAConfigurationActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class TwoFAConfigurationActivity : AppCompatActivity() { 

    private val binding by lazy { ActivityTwofaConfigurationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        requestConfigurationKey()
    }

    private fun requestConfigurationKey() {
        val uuid = intent.getStringExtra("uuid").orEmpty()
        val password = intent.getStringExtra("password").orEmpty()

        val user = User()
        user.onCodeSuccess = {
            val key = it.getJSONObject("data").getString("totpId")
            runOnUiThread {
                showTotpCode(key)
            }
        }

        user.onCodeError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        user.code(
            activity = this,
            uuid = uuid,
            password = password,
            type = "totp"
        )
    }

    private fun showTotpCode(key: String) {
        binding.requestText.text = getString(com.tetrapi.or.R.string.activity_twofa_configuration_description)
        binding.requestLoading.visibility = View.GONE

        binding.totpLayout.visibility = View.VISIBLE
        binding.totpText.text = key.chunked(4).joinToString(" ")
        binding.totpCard.setOnClickListener {
            copyToClipboard(key)
        }

        binding.validateButton.setOnClickListener {
            val code = binding.twofaEdit.text.toString().trim()
            validate(code)
        }
    }

    private fun copyToClipboard(code: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TOTP Code", code)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, code, Toast.LENGTH_LONG).show()
    }

    private fun validate(code: String) {
        val user = User()
        user.onValidateSuccess = {
            val message = it.getJSONObject("data").getString("message")
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }

            finish()
        }

        user.onValidateError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        val uuid = intent.getStringExtra("uuid").orEmpty()
        val password = intent.getStringExtra("password").orEmpty()
        user.validate(
            activity = this,
            uuid = uuid,
            password = password,
            code = code,
            type = "totp"
        )
    }
}