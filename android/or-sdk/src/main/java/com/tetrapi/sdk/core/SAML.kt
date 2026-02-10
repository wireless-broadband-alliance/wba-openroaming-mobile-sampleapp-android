package com.tetrapi.sdk.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import com.tetrapi.sdk.R
import com.tetrapi.sdk.utils.Preferences
import com.tetrapi.sdk.utils.Web
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import androidx.core.net.toUri

//
//  SAML.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

class SAML(private val activity: AppCompatActivity) {

    fun start() {
        val url = Preferences(activity).getString("SAML_START") ?: return onSAMLError("TODO handle empty start!")
        CustomTabsIntent.Builder().build().launchUrl(activity, url.toUri())

    }

    var onCodeSuccess: (response: JSONObject) -> Unit = {
        /** Code succeeded */
    }

    var onSAMLError: (message: String?) -> Unit = {
        /** Code failed */
    }
}