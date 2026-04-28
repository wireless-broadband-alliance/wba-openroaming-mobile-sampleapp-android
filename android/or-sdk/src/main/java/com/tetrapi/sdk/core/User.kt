package com.tetrapi.sdk.core

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.tetrapi.sdk.utils.Preferences
import com.tetrapi.sdk.R
import com.tetrapi.sdk.utils.Web
import com.tetrapi.sdk.utils.isMissing2FA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

//
//  User.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
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

    fun code(activity: AppCompatActivity, uuid: String, password: String, type: String) {
        if (uuid.isBlank() || password.isBlank()) {
            val message = activity.getString(R.string.open_roaming_login_blank_credentials)
            return onCodeError(message)
        }

        val params = JSONObject().apply {
            put("uuid", uuid)
            put("password", password)
        }

        code(activity, params, type)
    }

    private fun code(activity: AppCompatActivity, params: JSONObject, type: String) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val siteKey = Preferences(activity).getString("TURNSTILE_SITE_KEY")
        if (siteKey.isNullOrBlank()) {
            val message = activity.getString(R.string.common_error_turnstile_key)
            return@launch onCodeError(message)
        }

        val token = Turnstile().getToken(activity, siteKey)
        params.put("turnstile_token", token)

        val request = runCatching {
            val web = Web(activity)
            web.code(params, type)
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success) onCodeSuccess(it) else onCodeError(it.getString("error"))
        }

        request.onFailure {
            onCodeError(it.message)
        }
    }

    var onCodeSuccess: (response: JSONObject) -> Unit = {
        /** Code succeeded */
    }

    var onCodeError: (message: String?) -> Unit = {
        /** Code failed */
    }

    fun validate(activity: AppCompatActivity, uuid: String, password: String, code: String, type: String) {
        if (code.isBlank()) {
            val message = activity.getString(R.string.open_roaming_2fa_blank_credentials)
            return onValidateError(message)
        }

        val params = JSONObject().apply {
            put("uuid", uuid)
            put("password", password)
            put("code", code)
            put("type", type)
        }

        validate(activity, params)
    }

    private fun validate(activity: AppCompatActivity, params: JSONObject) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val request = runCatching {
            val web = Web(activity)
            web.validate(params)
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success) onValidateSuccess(it) else onValidateError(it.getString("error"))
        }

        request.onFailure {
            onValidateError(it.message)
        }
    }

    var onValidateSuccess: (response: JSONObject) -> Unit = {
        /** Validate succeeded */
    }

    var onValidateError: (message: String?) -> Unit = {
        /** Validate failed */
    }

    fun register(activity: AppCompatActivity, email: String, password: String, nameFirst: String? = null, nameLast: String? = null, acceptedTOS: Boolean = true) {
        if (email.isBlank() || password.isBlank()) {
            val message = activity.getString(R.string.open_roaming_register_blank_credentials)
            return onRegisterError(message)
        }

        if (!acceptedTOS) {
            val message = activity.getString(R.string.open_roaming_register_terms)
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

        val token = Turnstile().getToken(activity, siteKey)
        params.put("turnstile_token", token)

        val request = runCatching {
            val web = Web(activity)
            web.register(params, type)
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success) onRegisterSuccess(it) else onRegisterError(it.getString("error"))
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

    fun login(activity: AppCompatActivity, uuid: String, password: String, code: String? = null) {
        if (uuid.isBlank() || password.isBlank()) {
            val message = activity.getString(R.string.open_roaming_login_blank_credentials)
            return onLoginError(message)
        }

        val params = JSONObject().apply {
            put("uuid", uuid)
            put("password", password)
            if (code != null) put("twoFACode", code)
        }

        login(activity, params, "local")
    }

    fun login(activity: AppCompatActivity, response: String) {
        val params = JSONObject().apply {
            put("response", response)
        }

        login(activity, params, "saml")
    }

    private fun login(activity: AppCompatActivity, params: JSONObject, type: String) = activity.lifecycleScope.launch(Dispatchers.IO) {
        if (type === "local") {
            val siteKey = Preferences(activity).getString("TURNSTILE_SITE_KEY")
            if (siteKey.isNullOrBlank()) {
                val message = activity.getString(R.string.common_error_turnstile_key)
                return@launch onLoginError(message)
            }

            val token = Turnstile().getToken(activity, siteKey)
            params.put("turnstile_token", token)
        }

        val request = runCatching {
            val web = Web(activity)
            web.login(params, type)
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success) it.getJSONObject("data").saveUserLogin(activity)
            else if (it.isMissing2FA()) onLoginMissing2FA()
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

    var onLoginMissing2FA: () -> Unit = {
        /** Login Missing 2FA */
    }

    var onLoginError: (message: String?) -> Unit = {
        /** Login failed */
    }

    fun reset(activity: AppCompatActivity, email: String) = activity.lifecycleScope.launch(Dispatchers.IO) {
        if (email.isBlank()) {
            val message = activity.getString(R.string.open_roaming_reset_blank_credentials)
            return@launch onResetError(message)
        }

        val siteKey = Preferences(activity).getString("TURNSTILE_SITE_KEY")
        if (siteKey.isNullOrBlank()) {
            val message = activity.getString(R.string.common_error_turnstile_key)
            return@launch onRegisterError(message)
        }

        val token = Turnstile().getToken(activity, siteKey)
        val params = JSONObject().apply {
            put("email", email)
            put("turnstile_token", token)
        }

        val request = runCatching {
            val web = Web(activity)
            web.reset(params)
        }

        request.onSuccess {
            val success = it.getBoolean("success")
            if (success) onResetSuccess(it) else onResetError(it.getString("error"))
        }

        request.onFailure {
            onResetError(it.message)
        }
    }

    var onResetSuccess: (response: JSONObject) -> Unit = {
        /** Reset succeeded */
    }

    var onResetError: (message: String?) -> Unit = {
        /** Reset failed */
    }

    fun info(activity: FragmentActivity) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val token = jwtToken(activity)
            ?: return@launch onInfoError(activity.getString(R.string.common_error_jwt_token))

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