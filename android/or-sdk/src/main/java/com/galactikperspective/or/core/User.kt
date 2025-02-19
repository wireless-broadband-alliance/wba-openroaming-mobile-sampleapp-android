package com.galactikperspective.or.core

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.galactikperspective.or.utils.Preferences
import com.galactikperspective.or.R
import com.galactikperspective.or.utils.Web
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

//
//  User.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2025 Galactik Perspective. All rights reserved.
//

class User {

    fun register(activity: AppCompatActivity, email: String, password: String, nameFirst: String? = null, nameLast: String? = null) {
        if (email.isBlank() || password.isBlank()) {
            val message = activity.getString(R.string.open_roaming_register_blank_credentials)
            return onRegisterError(message)
        }

        val params = JSONObject().apply {
            put("email", email)
            put("password", password)
            if (nameFirst != null) put("first_name", nameFirst)
            if (nameLast != null) put("last_name", nameLast)
        }

        register(activity, params, "local")
    }

    private fun register(activity: AppCompatActivity, params: JSONObject, type: String) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val siteKey = Preferences(activity).getString("TURNSTILE_SITE_KEY")
        if (siteKey.isNullOrBlank()) {
            val message = activity.getString(R.string.common_error_turnstile_key)
            return@launch onRegisterError(message)
        }

        //TODO
        //val token = Turnstile().getToken(activity, siteKey)
        val token = "openroaming"
        params.put("turnstile_token", token)

        val request = runCatching {
            val web = Web(activity)
            web.register(params, type)
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success) onRegisterSuccess(it) else onRegisterError (it.getString("error"))
        }

        request.onFailure {
            onRegisterError(it.message)
        }
    }

    var onRegisterSuccess: (response: JSONObject) -> Unit = {
        /** Register succeeded */
    }

    var onRegisterError: (message: String?) -> Unit = {
        /** Register failed */
    }

    fun login(activity: AppCompatActivity, email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            val message = activity.getString(R.string.open_roaming_login_blank_credentials)
            return onLoginError(message)
        }

        val params = JSONObject().apply {
            put("uuid", email)
            put("password", password)
        }

        login(activity, params, "local")
    }

    private fun login(activity: AppCompatActivity, params: JSONObject, type: String) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val siteKey = Preferences(activity).getString("TURNSTILE_SITE_KEY")
        if (siteKey.isNullOrBlank()) {
            val message = activity.getString(R.string.common_error_turnstile_key)
            return@launch onRegisterError(message)
        }

        //TODO
        //val token = Turnstile().getToken(activity, siteKey)
        val token = "openroaming"
        params.put("turnstile_token", token)

        val request = runCatching {
            val web = Web(activity)
            web.login(params, type)
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success) onLoginSuccess(it) else onLoginError (it.getString("error"))
        }

        request.onFailure {
            onLoginError(it.message)
        }
    }

    var onLoginSuccess: (response: JSONObject) -> Unit = {
        /** Login succeeded */
    }

    var onLoginError: (message: String?) -> Unit = {
        /** Login failed */
    }
}