package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.tetrapi.or.databinding.ActivityResetBinding
import com.tetrapi.sdk.R
import com.tetrapi.sdk.core.User

//
//  ResetActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class ResetActivity : ORActivity() {

    private val binding by lazy { ActivityResetBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.resetButton.setOnClickListener {
            reset()
        }
    }

    private fun reset() {
        binding.resetButton.isLoading()

        val user = User()
        user.onResetSuccess = {
            val message = it.getJSONObject("data").getString("message")
            setResult(RESULT_OK, Intent().apply {
                putExtra("message", message)
            })

            finish()
        }

        user.onResetError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                binding.resetButton.isReady()
                Snackbar.make(binding.resetButton, message, 4000).show()
            }
        }

        user.reset(
            activity = this,
            email = binding.emailEdit.text.toString().trim()
        )
    }
}