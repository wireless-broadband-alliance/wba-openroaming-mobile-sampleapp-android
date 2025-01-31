package com.galactikperspective.or.core

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.galactikperspective.or.utils.Preferences
import com.galactikperspective.or.utils.Turnstile
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

    companion object {

        fun isLoggedIn(context: Context) = Preferences(context).getBoolean("isLoggedIn")
    }

    fun register(activity: AppCompatActivity, email: String, password: String, nameFirst: String, nameLast: String) {
        if (email.isBlank() || password.isBlank() || nameFirst.isBlank() || nameLast.isBlank()) {
            val message = activity.getString(R.string.open_roaming_register_blank_credentials)
            return onRegisterError(message)
        }

        val params = JSONObject().apply {
            put("email", email)
            put("password", password)
            put("first_name", nameFirst)
            put("last_name", nameLast)
        }

        register(activity, params, "local")
    }

    private fun register(activity: AppCompatActivity, params: JSONObject, type: String) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val siteKey = Preferences(activity).getString("TURNSTILE_SITE_KEY")
        if (siteKey.isNullOrBlank()) {
            val message = activity.getString(R.string.open_roaming_register_invalid_turnstile_key)
            return@launch onRegisterError(message)
        }

        val token = Turnstile().getToken(activity, siteKey)
        params.put("turnstile_token", token)

        val request = runCatching {
            val web = Web(activity)
            web.register(params, type)
        }

        request.onSuccess {
            print("test")
        }

        request.onFailure {
            onRegisterError(it.message)
        }
    }

    var onRegisterSuccess: (response: String) -> Unit = {
        /** Register succeeded */
    }

    var onRegisterError: (message: String?) -> Unit = {
        /** Register failed */
    }

    fun login(activity: AppCompatActivity, email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            val exception = IllegalArgumentException(activity.getString(R.string.open_roaming_login_blank_credentials))
            return onLoginError(exception)
        }

        val params = JSONObject().apply {
            put("uuid", email)
            put("password", password)
        }

        login(activity, params, "local")
    }

    private fun login(activity: AppCompatActivity, params: JSONObject, type: String) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val token = Turnstile().getToken(activity, "1x00000000000000000000AA")
        print(token)

        //params.put("cf-turnstile-response", token)

        //Web().login(activity, this@User, params, type)
    }

    var onLoginSuccess: (response: String) -> Unit = {
        /** Login succeeded */
    }

    var onLoginError: (exception: Exception) -> Unit = {
        /** Login failed */
    }
}