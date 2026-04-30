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
//  Copyright © 2026 Tetrapi. All rights reserved.
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

        fun isAuthActive(context: Context, type: String): Boolean {
            val preferences = Preferences(context)
            return isAuthActive(preferences, type)
        }

        fun isAuthActive(preferences: Preferences, type: String) = preferences.getBoolean(type)
    }

    fun info(activity: AppCompatActivity, expiration: Int = 2) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val request = runCatching {
            val web = Web(activity)
            web.getConfig()
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success) save(activity, expiration, it.getJSONObject("data"))  else onInfoError (it.getString("error"))
        }

        request.onFailure {
            onInfoError(it.message)
        }
    }

    private fun save(activity: AppCompatActivity, expiration: Int, data: JSONObject) {
        val preferences = Preferences(activity)
        data.getJSONObject("platform").apply {
            val tos = getString("TOS")
            preferences.saveString("TOS", tos)

            val privacy = getString("PRIVACY_POLICY")
            preferences.saveString("PRIVACY_POLICY", privacy)
        }

        data.getJSONObject("turnstile").apply {
            val siteKey = getString("TURNSTILE_KEY")
            preferences.saveString("TURNSTILE_SITE_KEY", siteKey)
        }

        data.getJSONObject("auth").apply {
            val local = getBoolean("AUTH_METHOD_LOGIN_TRADITIONAL_ENABLED")
            preferences.saveBoolean("AUTH_LOCAL", local)

            val saml = getBoolean("AUTH_METHOD_SAML_ENABLED")
            preferences.saveBoolean("AUTH_SAML", saml)
        }

        data.getJSONObject("saml").apply {
            //TODO get this from API?
            preferences.saveString("SAML_START", "https://wifi.tetrapi.pt/saml/login")
        }

        val expiration = System.currentTimeMillis() + (expiration * 24 * 60 * 60 * 1000)
        preferences.saveLong("CONFIG_EXPIRATION", expiration)

        onInfoSuccess()
    }

    var onInfoSuccess: () -> Unit = {
        /** Config info succeeded */
    }

    var onInfoError: (message: String?) -> Unit = {
        /** Config info failed */
    }
}