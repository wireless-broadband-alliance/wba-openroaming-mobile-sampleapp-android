package com.tetrapi.sdk.core

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tetrapi.sdk.utils.Preferences
import com.tetrapi.sdk.utils.Web
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

//
//  Config.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Tetrapi. All rights reserved.
//

class Config {

    companion object {

        fun expired(context: Context): Boolean {
            val preferences = Preferences(context)
            return expired(preferences)
        }

        fun expired(preferences: Preferences): Boolean {
            val expiration = preferences.getLong("CONFIG_EXPIRATION")
            return expiration == 0L || System.currentTimeMillis() > expiration
        }

        fun tos(context: Context): String? {
            val preferences = Preferences(context)
            return tos(preferences)
        }

        fun tos(preferences: Preferences) = preferences.getString("TOS")

        fun privacy(context: Context): String? {
            val preferences = Preferences(context)
            return privacy(preferences)
        }

        fun privacy(preferences: Preferences) = preferences.getString("PRIVACY_POLICY")
    }

    fun info(activity: AppCompatActivity, expiration: Int = 2) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val request = runCatching {
            val web = Web(activity)
            web.getConfig()
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success)save(activity, expiration, it)  else onInfoError (it.getString("error"))
        }

        request.onFailure {
            onInfoError(it.message)
        }
    }

    private fun save(activity: AppCompatActivity, expiration: Int, data: JSONObject) {
        val preferences = Preferences(activity)

        val siteKey = data.getJSONObject("data").getJSONObject("turnstile").getString("TURNSTILE_KEY")
        preferences.saveString("TURNSTILE_SITE_KEY", siteKey)

        val expiration = System.currentTimeMillis() + (expiration * 24 * 60 * 60 * 1000)
        preferences.saveLong("CONFIG_EXPIRATION", expiration)

        val tos = data.getJSONObject("data").getJSONObject("platform").getString("TOS")
        preferences.saveString("TOS", tos)

        val privacy = data.getJSONObject("data").getJSONObject("platform").getString("PRIVACY_POLICY")
        preferences.saveString("PRIVACY_POLICY", privacy)

        onInfoSuccess()
    }

    var onInfoSuccess: () -> Unit = {
        /** Config info succeeded */
    }

    var onInfoError: (message: String?) -> Unit = {
        /** Config info failed */
    }
}