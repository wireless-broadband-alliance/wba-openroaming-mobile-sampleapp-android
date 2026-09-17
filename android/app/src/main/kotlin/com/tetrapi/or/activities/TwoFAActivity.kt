package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.tetrapi.or.databinding.ActivityTwofaBinding
import com.tetrapi.sdk.core.SDK
import com.tetrapi.sdk.utils.allowInfiniteLines
import kotlinx.coroutines.launch
import kotlin.toString

//
//  TwoFAActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class TwoFAActivity : ORActivity() {

    private val binding by lazy { ActivityTwofaBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val uuid = intent.getStringExtra("uuid").orEmpty()
        val password = intent.getStringExtra("password").orEmpty()

        binding.loginButton.setOnClickListener {
            getToken(uuid, password, "login")
        }

        binding.requestText.setOnClickListener {
            getToken(uuid, password, "code")
        }

        binding.totpText.setOnClickListener {
            configureTotpCode(uuid, password)
        }
    }

    private fun getToken(uuid: String, password: String, type: String) = lifecycleScope.launch {
        if (type == "login") binding.loginButton.isLoading()
        else binding.requestText.visibility = View.GONE

        val result = SDK.getTurnstile().getToken(
            activity = this@TwoFAActivity
        )

        result.onSuccess {
            if (type == "login") login(uuid, password, it)
            else requestEmailCode(uuid, password, it)
        }

        result.onFailure { error ->
            if (type == "login") binding.loginButton.isReady()
            else binding.requestText.visibility = View.VISIBLE

            Snackbar.make(binding.loginButton, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }

    private fun login(uuid: String, password: String, token: String) = lifecycleScope.launch {
        val result = SDK.getUser().login(
            uuid = uuid,
            password = password,
            code = binding.twofaEdit.text.toString().trim(),
            token = token
        )

        result.onSuccess {
            startActivity(Intent(this@TwoFAActivity, MainActivity::class.java))
            finish()
        }

        result.onFailure { error ->
            binding.loginButton.isReady()
            Snackbar.make(binding.loginButton, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }

    private fun requestEmailCode(uuid: String, password: String, token: String) = lifecycleScope.launch {
        val result = SDK.getUser().code(
            uuid = uuid,
            password = password,
            type = "email",
            token = token
        )

        result.onSuccess {
            binding.requestText.visibility = View.VISIBLE
            Snackbar.make(binding.loginButton, it, 4000).allowInfiniteLines().show()
        }

        result.onFailure { error ->
            binding.requestText.visibility = View.VISIBLE
            Snackbar.make(binding.requestText, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }

    private fun configureTotpCode(uuid: String, password: String) {
        val intent = Intent(this, TwoFAConfigurationActivity::class.java).apply {
            putExtra("uuid", uuid)
            putExtra("password", password)
        }

        actionLauncher.launch(intent)
    }

    private val actionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        if (it.resultCode == RESULT_OK) {
            val message = it.data?.getStringExtra("message")
            if (message != null) Snackbar.make(binding.loginButton, message, 8000).allowInfiniteLines().show()
        }
    }
}