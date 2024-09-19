package com.galactikperspective.or.core

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.galactikperspective.or.R
import com.galactikperspective.or.utils.Turnstile
import com.galactikperspective.or.utils.Web
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

//
//  User.kt
//  Open Roaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2024 Galactik Perspective. All rights reserved.
//

class User {

    fun login(activity: AppCompatActivity, email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            val message = activity.getString(R.string.open_roaming_login_blank_credentials)
            return onLoginError(IOException(message))
        }

        val params = JSONObject().apply {
            put("uuid", email)
            put("password", password)
        }

        login(activity, params, "local")
    }

    private fun login(activity: AppCompatActivity, params: JSONObject, type: String) = activity.lifecycleScope.launch(Dispatchers.IO) {
        val token = Turnstile().getToken(activity)
        params.put("cf-turnstile-response", token)

        Web().login(activity, this@User, params, type)
    }

    var onLoginSuccess: (response: String) -> Unit = {
        /** Login succeeded */
    }

    var onLoginError: (exception: Exception) -> Unit = {
        /** Login failed */
    }

    fun register(activity: AppCompatActivity) {

    }
}