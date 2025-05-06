package com.galactikperspective.mimwifi.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.galactikperspective.mimwifi.databinding.ActivityConfigBinding
import com.galactikperspective.or.utils.Web
import kotlinx.coroutines.launch
import com.galactikperspective.or.R
import com.galactikperspective.or.utils.Preferences
import org.json.JSONObject

//
//  ConfigActivity.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Galactik Perspective. All rights reserved.
//

class ConfigActivity : AppCompatActivity() {

    private val binding by lazy { ActivityConfigBinding.inflate(layoutInflater) }
    private val preferences by lazy { Preferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkConfig()
    }

    private fun checkConfig() {
        val turnstileKey = preferences.getString("TURNSTILE_SITE_KEY")
        if (turnstileKey.isNullOrBlank()) return setContentView()

        val expiration = preferences.getLong("CONFIG_EXPIRATION")
        if (expiration == 0L || System.currentTimeMillis() > expiration) return setContentView()

        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun setContentView() {
        setContentView(binding.root)
        setViews()
    }

    private fun setViews() = lifecycleScope.launch {
        val request = runCatching {
            val web = Web(baseContext)
            web.getConfig()
        }

        request.onSuccess {
            //TODO this should be done by the SDK module
            saveData(it)
        }

        request.onFailure {
            binding.descriptionText.text = it.message ?: getString(R.string.common_error)
            binding.descriptionText.setTextColor(Color.RED)
        }
    }

    private fun saveData(response: JSONObject) {
        val success = response.getBoolean("success")
        if (!success) {
            binding.descriptionText.text = response.getString("error") ?: getString(R.string.common_error)
            binding.descriptionText.setTextColor(Color.RED)
            return
        }

        val siteKey = response.getJSONObject("data").getJSONObject("turnstile").getString("TURNSTILE_KEY")
        preferences.saveString("TURNSTILE_SITE_KEY", siteKey)

        val expiration = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000) // Current time plus 2 days
        preferences.saveLong("CONFIG_EXPIRATION", expiration)

        val tos = response.getJSONObject("data").getJSONObject("platform").getString("TOS")
        preferences.saveString("TOS", tos)

        val privacy = response.getJSONObject("data").getJSONObject("platform").getString("PRIVACY_POLICY")
        preferences.saveString("PRIVACY_POLICY", privacy)

        startActivity(Intent(this, LoginActivity::class.java))
    }
}