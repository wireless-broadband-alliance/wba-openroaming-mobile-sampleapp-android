package com.tetrapi.or.activities

import android.os.Bundle
import android.widget.Toast
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
            runOnUiThread {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }

            finish()
        }

        user.onResetError = {
            val message = it ?: getString(R.string.common_error)
            runOnUiThread {
                binding.resetButton.isReady()
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }

            binding.resetButton.isReady()
        }

        user.reset(
            activity = this,
            email = binding.emailEdit.text.toString().trim()
        )
    }
}