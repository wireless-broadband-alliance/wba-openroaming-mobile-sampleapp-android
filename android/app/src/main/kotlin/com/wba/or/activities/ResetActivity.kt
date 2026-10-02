package com.wba.or.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.wba.or.databinding.ActivityResetBinding
import com.wba.sdk.core.SDK
import com.wba.sdk.utils.allowInfiniteLines
import kotlinx.coroutines.launch

//
//  ResetActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 WBA. All rights reserved.
//

class ResetActivity : ORActivity() {

    private val binding by lazy { ActivityResetBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.resetButton.setOnClickListener {
            getToken()
        }
    }

    private fun getToken() = lifecycleScope.launch {
        binding.resetButton.isLoading()

        val result = SDK.getTurnstile().getToken(
            activity = this@ResetActivity
        )

        result.onSuccess {
            reset(it)
        }

        result.onFailure { error ->
            binding.resetButton.isReady()
            Snackbar.make(binding.resetButton, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }

    private fun reset(token: String) = lifecycleScope.launch {
        val result = SDK.getUser().reset(
            uuid = binding.emailEdit.text.toString().trim(),
            token = token
        )

        result.onSuccess {
            setResult(RESULT_OK, Intent().apply {
                putExtra("message", it)
            })

            finish()
        }

        result.onFailure { error ->
            binding.resetButton.isReady()
            Snackbar.make(binding.resetButton, error.message.toString(), 4000).allowInfiniteLines().show()
        }
    }
}