package com.tetrapi.or.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.tetrapi.or.R
import com.tetrapi.or.databinding.ActivityUserBinding
import com.tetrapi.sdk.core.SDK
import kotlinx.coroutines.launch
import org.json.JSONObject

//
//  UserActivity.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class UserActivity : ORActivity() {

    private val binding by lazy { ActivityUserBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setViews()
    }

    private fun setViews() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        fetchInfo()
    }

    private fun fetchInfo() = lifecycleScope.launch {
        val result = SDK.getUser().info()
        result.onSuccess {
            setInfo(it)
        }

        result.onFailure { error ->
            val message = error.message.toString()
            Toast.makeText(this@UserActivity, message, Toast.LENGTH_LONG).show()

            if (message == "JWT Token is expired!") sessionExpired()
        }
    }

    private fun setInfo(data: JSONObject) {
        binding.detailsLayout.visibility = View.VISIBLE

        binding.uuidText.text = data.getString("uuid")
        binding.typeText.text = data.getJSONArray("user_external_auths").getJSONObject(0).getString("provider")

        binding.createdText.text = String.format(getString(R.string.activity_user_created), data.getString("created_at"))
    }

    private fun sessionExpired() {
        setResult(RESULT_OK, Intent().apply {
            putExtra("expired", true)
        })

        finish()
    }
}