package com.tetrapi.or.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.tetrapi.or.databinding.ActivityTwofaConfigurationBinding
import com.tetrapi.sdk.core.SDK
import com.tetrapi.sdk.utils.allowInfiniteLines
import kotlinx.coroutines.launch

//
//  TwoFAConfigurationActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class TwoFAConfigurationActivity : ORActivity() {

    private val binding by lazy { ActivityTwofaConfigurationBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val uuid = intent.getStringExtra("uuid").orEmpty()
        val password = intent.getStringExtra("password").orEmpty()
        getToken(uuid, password)
    }

    private fun getToken(uuid: String, password: String) = lifecycleScope.launch {
        val result = SDK.getTurnstile().getToken(
            activity = this@TwoFAConfigurationActivity
        )

        result.onSuccess {
            requestConfigurationKey(uuid, password, it)
        }

        result.onFailure { error ->
            Snackbar.make(binding.requestLoading, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }

    private fun requestConfigurationKey(uuid: String, password: String, token: String) = lifecycleScope.launch {
        val result = SDK.getUser().code(
            uuid = uuid,
            password = password,
            type = "totp",
            token = token
        )

        result.onSuccess {
            showTotpCode(uuid, password, it)
        }

        result.onFailure { error ->
            Snackbar.make(binding.validateButton, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }

    private fun showTotpCode(uuid: String, password: String, key: String) {
        binding.requestText.text = getString(com.tetrapi.or.R.string.activity_twofa_configuration_description)
        binding.requestLoading.visibility = View.GONE

        binding.totpLayout.visibility = View.VISIBLE
        binding.totpText.text = key.chunked(4).joinToString(" ")
        binding.totpCard.setOnClickListener {
            copyToClipboard(key)
        }

        binding.validateButton.setOnClickListener {
            val code = binding.twofaEdit.text.toString().trim()
            validate(uuid, password, code)
        }
    }

    private fun copyToClipboard(code: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TOTP Code", code)
        clipboard.setPrimaryClip(clip)

        Snackbar.make(binding.validateButton, code, 4000).allowInfiniteLines().show()
    }

    private fun validate(uuid: String, password: String, code: String) = lifecycleScope.launch {
        binding.validateButton.isLoading()
        val result = SDK.getUser().validate(
            uuid = uuid,
            password = password,
            code = code
        )

        result.onSuccess {
            setResult(RESULT_OK, Intent().apply {
                putExtra("message", it)
            })

            finish()
        }

        result.onFailure { error ->
            Snackbar.make(binding.validateButton, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }
}