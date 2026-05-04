package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import com.google.android.material.snackbar.Snackbar
import com.tetrapi.or.databinding.ActivityTwofaBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.User
import kotlin.text.isNotEmpty

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
            login(uuid, password)
        }

        binding.requestText.setOnClickListener {
            requestEmailCode(uuid, password)
        }

        binding.totpText.setOnClickListener {
            configureTotpCode(uuid, password)
        }
    }

    private fun login(uuid: String, password: String) {
        binding.loginButton.isLoading()

        val user = User()
        user.onLoginSuccess = {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        user.onLoginError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                binding.loginButton.isReady()
                Snackbar.make(binding.loginButton, message, 4000).show()
            }
        }

        user.login(
            this,
            uuid = uuid,
            password = password,
            code = binding.twofaEdit.text.toString().trim().takeIf { it.isNotEmpty() }
        )
    }

    private fun requestEmailCode(uuid: String, password: String) {
        if (binding.requestText.isGone) return
        binding.requestText.visibility = View.GONE

        val user = User()
        user.onCodeSuccess = {
            val message = it.getJSONObject("data").getString("message")
            runOnUiThread {
                binding.requestText.visibility = View.VISIBLE
                Snackbar.make(binding.loginButton, message, 4000).show()
            }
        }

        user.onCodeError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                binding.requestText.visibility = View.VISIBLE
                Snackbar.make(binding.loginButton, message, 4000).show()
            }
        }

        user.code(
            activity = this,
            uuid = uuid,
            password = password,
            type = "email"
        )
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
            if (message != null) Snackbar.make(binding.loginButton, message, 8000).show()
        }
    }
}