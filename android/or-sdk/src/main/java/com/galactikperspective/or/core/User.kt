package com.galactikperspective.or.core

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.galactikperspective.or.utils.Preferences
import com.galactikperspective.or.R
import com.galactikperspective.or.utils.Turnstile
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

        fun jwtToken(context: Context): String? {
            val preferences = Preferences(context)
            return jwtToken(preferences)
        }

        fun jwtToken(preferences: Preferences) = preferences.getString("USER_JWT_TOKEN")

        fun setUserJwtToken(preferences: Preferences, token: String) {
            preferences.saveString("USER_JWT_TOKEN", token)
        }

        fun isLoggedIn(context: Context): Boolean {
            val preferences = Preferences(context)
            return isLoggedIn(preferences)
        }

        fun isLoggedIn(preferences: Preferences) = preferences.getBoolean("USER_LOGGED_IN")

        fun setUserLoggedIn(preferences: Preferences, isLoggedIn: Boolean = true) {
            preferences.saveBoolean("USER_LOGGED_IN", isLoggedIn)
        }
    }

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

        //val token = "openroaming"
        val token = Turnstile().getToken(activity, siteKey)
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

    fun login(activity: AppCompatActivity, email: String, password: String, code: String? = null) {
        if (email.isBlank() || password.isBlank()) {
            val message = activity.getString(R.string.open_roaming_login_blank_credentials)
            return onLoginError(message)
        }

        val params = JSONObject().apply {
            put("uuid", email)
            put("password", password)
            if (code != null) put("twoFACode", code)
        }

        login(activity, params, "local")
    }

    private fun login(activity: AppCompatActivity, params: JSONObject, type: String) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val siteKey = Preferences(activity).getString("TURNSTILE_SITE_KEY")
        if (siteKey.isNullOrBlank()) {
            val message = activity.getString(R.string.common_error_turnstile_key)
            return@launch onLoginError(message)
        }

        //val token = "openroaming"
        val token = Turnstile().getToken(activity, siteKey)
        params.put("turnstile_token", token)

        val request = runCatching {
            val web = Web(activity)
            web.login(params, type)
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success) it.getJSONObject("data").saveUserLogin(activity)
            else onLoginError(it.getString("error"))
        }

        request.onFailure {
            onLoginError(it.message)
        }
    }

    private fun JSONObject.saveUserLogin(activity: AppCompatActivity) {
        val preferences = Preferences(activity)
        setUserLoggedIn(preferences, true)

        val token = getString("token")
        setUserJwtToken(preferences, token)

        onLoginSuccess()
    }

    var onLoginSuccess: () -> Unit = {
        /** Login succeeded */
    }

    var onLoginError: (message: String?) -> Unit = {
        /** Login failed */
    }

    fun info(activity: FragmentActivity) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val token = jwtToken(activity)
        if (token == null) return@launch onInfoError(activity.getString(R.string.common_error_jwt_token))

        val request = runCatching {
            val web = Web(activity)
            web.getUser(token)
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            activity.runOnUiThread {
                if (success) onInfoSuccess(it.getJSONObject("data"))
                else onInfoError(it.getString("error"))
            }
        }

        request.onFailure {

            activity.runOnUiThread {
                onInfoError(it.message)
            }
        }
    }

    var onInfoSuccess: (data: JSONObject) -> Unit = {
        /** Info succeeded */
    }

    var onInfoError: (message: String?) -> Unit = {
        /** Info failed */
    }

    fun logout(activity: Activity) {
        val preferences = Preferences(activity)
        setUserLoggedIn(preferences, false)
    }
}