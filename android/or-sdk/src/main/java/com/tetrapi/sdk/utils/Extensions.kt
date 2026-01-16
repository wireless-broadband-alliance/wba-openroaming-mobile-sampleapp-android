package com.tetrapi.sdk.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import org.json.JSONObject

//
//  Extensions.kt
//  OpenRoaming SDK
//
//  Created by Fábio Carvalho
//  Copyright © 2026 Tetrapi. All rights reserved.
//

fun Context.isAppDebuggable() = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

fun JSONObject.isMissing2FA() = getString("error") == "Invalid data: Missing required fields."
        && getJSONArray("missing_fields").length() == 1
        && getJSONArray("missing_fields").get(0) == "twoFACode"